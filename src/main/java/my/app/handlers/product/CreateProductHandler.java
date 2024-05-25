package my.app.handlers.product;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import my.app.helpers.RedisKeyInvalidate;
import my.app.models.product.Product;
import my.app.services.product.ProductServiceImpl;

public class CreateProductHandler implements Handler{
    private ProductServiceImpl productService;
    private JedisPool jedisPool;

    @Inject
    public CreateProductHandler(ProductServiceImpl productService, JedisPool jedisPool) {
        this.productService = productService;
        this.jedisPool = jedisPool;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.parse(Map.class).then(data -> {
            try{
                Product product = new Product(0, (String) data.get("name"), (int) data.get("quantity"), (int) data.get("price"));
                int responseID = productService.createProduct(product);
                Map<String, Object> response = new HashMap<>();
                response.put("id", responseID);
                ctx.getResponse().status(Status.CREATED);
                response.put("message", "Product inserted successfully");

                // INVALIDATE REDIS
                try(Jedis jedis = jedisPool.getResource()){
                    RedisKeyInvalidate redisKeyInvalidate = new RedisKeyInvalidate(jedis);
                    redisKeyInvalidate.invalidateKeys("product_");
                }

                ctx.render(Jackson.json(response));
            } catch (Exception e) {
                // Handle exception
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Failed to create product");
                errorResponse.put("message", e.getMessage()); // Optional: Include exception message
                ctx.getResponse().status(Status.INTERNAL_SERVER_ERROR);
                ctx.render(Jackson.json(errorResponse));
            }
        });
    }
}
