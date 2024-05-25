package my.app;

import com.google.inject.AbstractModule;

import my.app.handlers.product.GetAllProductHandler;
import my.app.handlers.product.GetProductByIdHandler;
import my.app.handlers.product.UpdateProductHandler;
import my.app.handlers.product.CreateProductHandler;
import my.app.handlers.product.DeleteProductHandler;

public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetAllProductHandler.class);
        bind(GetProductByIdHandler.class);
        bind(CreateProductHandler.class);
        bind(UpdateProductHandler.class);
        bind(DeleteProductHandler.class);
    }
}
