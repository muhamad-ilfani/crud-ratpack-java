package my.app.handlers.product;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import my.app.models.product.Product;
import my.app.services.product.ProductServiceImpl;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

public class GetProductByIdHandler implements Handler{
    private ProductServiceImpl productService;

    @Inject
    public GetProductByIdHandler(ProductServiceImpl productService) {
        this.productService = productService;
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
            return;
        }

        try{
            Optional<Product> product = productService.getProductByID(id);
            
            // map inside data
            response.put("data", product.orElse(null));

            ctx.getResponse().status(Status.OK);
            ctx.render(Jackson.json(response));
        } catch (Exception e) {
            // Handle exception
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to find product");
            errorResponse.put("message", e.getMessage()); // Optional: Include exception message
            
            ctx.getResponse().status(Status.INTERNAL_SERVER_ERROR);
            ctx.render(Jackson.json(errorResponse));
            return;
        }
    }
}
