package my.app;

import ratpack.server.RatpackServer;
import ratpack.guice.Guice;

import my.app.db.DatabaseModule;
import my.app.handlers.product.GetAllProductHandler;
import my.app.handlers.product.GetProductByIdHandler;
import my.app.handlers.product.UpdateProductHandler;
import my.app.handlers.product.CreateProductHandler;
import my.app.handlers.product.DeleteProductHandler;

public class Main {
 public static void main(String... args) throws Exception {
    RatpackServer.start(server -> server
      .registry(Guice.registry(b -> {
        b.module(MyModule.class);
        b.module(DatabaseModule.class);
      }))
      .handlers(chain -> chain
        .get(ctx -> ctx.render("Hello World!"))
        .get("products", GetAllProductHandler.class)
        .post("product", CreateProductHandler.class)
        .get("product/:id", GetProductByIdHandler.class)
        .put("product/update/:id", UpdateProductHandler.class)
        .delete("product/delete/:id", DeleteProductHandler.class)
      )
    );
 }
}