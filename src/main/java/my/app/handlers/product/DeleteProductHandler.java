package my.app.handlers.product;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import ratpack.handling.Context;
import my.app.helpers.RedisKeyInvalidate;
import my.app.services.product.ProductServiceImpl;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class DeleteProductHandler implements Handler {
    private ProductServiceImpl productService;
    private JedisPool jedisPool;

    @Inject
    public DeleteProductHandler(ProductServiceImpl productService, JedisPool jedisPool) {
        this.productService = productService;
        this.jedisPool = jedisPool;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String idParam = ctx.getPathTokens().get("id");
        int id = 0;
        Map<String, Object> response = new HashMap<>();

        try{
            id = Integer.parseInt(idParam);
        }catch(NumberFormatException e) {
            response.put("message", "invalid parameter id");
            ctx.getResponse().status(Status.BAD_REQUEST);
            ctx.render(Jackson.json(response));
        }

        try{
            boolean isDeleted = productService.deleteProduct(id);
            
            // INVALIDATE REDIS
            try(Jedis jedis = jedisPool.getResource()){
                RedisKeyInvalidate redisKeyInvalidate = new RedisKeyInvalidate(jedis);
                redisKeyInvalidate.invalidateKeys("product_");
            }

            if(isDeleted){
                response.put("message", "Product deleted successfully");
                ctx.getResponse().status(Status.OK);
                ctx.render(Jackson.json(response));
            } else {
                response.put("message", "Product not found");
                ctx.getResponse().status(Status.NOT_FOUND);
                ctx.render(Jackson.json(response));
            }
        } catch (Exception e) {
            // Handle exception
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to find product");
            errorResponse.put("message", e.getMessage()); // Optional: Include exception message
            
            ctx.getResponse().status(Status.INTERNAL_SERVER_ERROR);
            ctx.render(Jackson.json(errorResponse));
        }
    }
}
