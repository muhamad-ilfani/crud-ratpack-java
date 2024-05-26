package my.app.services.product;

import java.util.List;
import java.util.Optional;

import my.app.models.product.Product;

public interface ProductService {
    List<Product> getAllProducts(int limit, int page, String sort) throws Exception;
    int countAllProducts() throws Exception;
    Optional<Product> getProductByID(int id) throws Exception;
    int createProduct(Product product) throws Exception;
    boolean updateProduct(Product product, int id) throws Exception;
    boolean deleteProduct(int id) throws Exception;
}
