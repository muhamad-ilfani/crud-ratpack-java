package my.app.services.product;

import javax.inject.Inject;
import javax.sql.DataSource;

import my.app.models.product.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.sql.Connection;

public class ProductServiceImpl implements ProductService {
    private DataSource dataSource;

    @Inject
    public ProductServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> getAllProducts(int limit, int page, String sort) throws Exception {
        try(Connection connection = dataSource.getConnection()){
            String[] sortList = sort.split(":");
            List<Product> products = new ArrayList<>();
            String sql = "SELECT * FROM servicea.products ";
            sql = sql + "ORDER BY " + sortList[0] + " " + sortList[1];
            sql = sql + " LIMIT ? OFFSET ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, limit);
                stmt.setInt(2, (page-1)*limit);
                try(ResultSet rs = stmt.executeQuery()) {
                    while(rs.next()) {
                        Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("price"));
                        products.add(product);
                    }
                }
            }

            return products;
        }
    }

    @Override
    public Optional<Product> getProductByID(int id) throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM servicea.products WHERE id = ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()) {
                        Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("price"));
                        return Optional.of(product);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        }
    }

    @Override
    public int createProduct(Product product) throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO servicea.products(name, quantity, price) VALUES (?, ?, ?) RETURNING id";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, product.getName());
                stmt.setInt(2, product.getQuantity());
                stmt.setInt(3, product.getPrice());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    } else {
                        throw new RuntimeException("No generated keys were returned");
                    }
                }
            }
        }
    }

    @Override
    public boolean updateProduct(Product product, int id) throws Exception {
        //Optional<Product> getProduct = this.getProductByID(id);
        try(Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE servicea.products SET name = ?, quantity = ?, price = ?, modify_at = now(), modify_by = 'SYSTEM' where id = ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, product.getName());
                stmt.setInt(2, product.getQuantity());
                stmt.setInt(3, product.getPrice());
                stmt.setInt(4, id);
                int rowsAffected = stmt.executeUpdate();

                return rowsAffected > 0;
            }
        }
    }

    @Override
    public boolean deleteProduct(int id) throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM servicea.products WHERE id = ?;";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();

                return rowsAffected > 0;
            }
        }
    }
}
