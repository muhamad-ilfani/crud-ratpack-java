package my.app.handlers.product;

import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ratpack.handling.Context;
import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.services.product.ProductServiceImpl;
import my.app.models.product.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetAllProductHandler implements Handler {
    private ProductServiceImpl productService;
    private JedisPool jedisPool;
    private ObjectMapper objectMapper;

    @Inject
    public GetAllProductHandler(ProductServiceImpl productService, JedisPool jedisPool, ObjectMapper objectMapper) {
        this.productService = productService;
        this.jedisPool = jedisPool;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        try(Jedis jedis = jedisPool.getResource()){
            Optional<String> limitParam = Optional.ofNullable(ctx.getRequest().getQueryParams().get("limit"));
            Optional<String> pageParam = Optional.ofNullable(ctx.getRequest().getQueryParams().get("page"));
            Optional<String> sortParam = Optional.ofNullable(ctx.getRequest().getQueryParams().get("sort"));
            Map<String, Object> response = new HashMap<>();

            int limit = 10; // default limit
            int page = 1; // default page

            if(limitParam.isPresent() && pageParam.isPresent()){
                limit = Integer.parseInt(limitParam.get());
                page = Integer.parseInt(pageParam.get());
            }

            // GET REDIS CACHE
            String redisKey = "product_" + limitParam.orElse(null) + pageParam.orElse(null) + sortParam.orElse(null);
            //jedis.del(redisKey);
            System.out.println(redisKey);
            String cachedResponse = jedis.get(redisKey);
            if (cachedResponse != null) {
                System.out.println("GET FROM CACHE");
                List<Product> getProductCache = objectMapper.readValue(cachedResponse, new TypeReference<List<Product>>() {});
                System.out.println(getProductCache);
                // map inside data
                response.put("data", getProductCache);
                response.put("total_entries", getProductCache.size());
                ctx.getResponse().status(Status.OK);
                ctx.render(Jackson.json(response));

                return;
            }

            System.out.println("GET FROM DB");

            // Setting sort param
            String sort = sortParam.orElse("id:desc");
            try{
                List<Product> products = productService.getAllProducts(limit, page, sort);
                // map inside data
                response.put("data", products);
                response.put("total_entries", products.size());

                // SET RESPONSE TO REDIS with TTL 30 Minutes
                jedis.setex(redisKey, 30*60, objectMapper.writeValueAsString(products));

                ctx.getResponse().status(Status.OK);
                ctx.render(Jackson.json(response));
            } catch (Exception e) {
                // Handle exception
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to find products");
                errorResponse.put("message", e.getMessage()); // Optional: Include exception message
                
                ctx.getResponse().status(Status.INTERNAL_SERVER_ERROR);
                ctx.render(Jackson.json(errorResponse));
            }
        }
    }
}
