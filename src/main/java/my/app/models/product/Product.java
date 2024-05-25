package my.app.models.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    private int id;
    private String name;
    private int quantity;
    private int price;

    public Product(
            @JsonProperty("id") int id, 
            @JsonProperty("name") String name, 
            @JsonProperty("quantity") int quantity, 
            @JsonProperty("price") int price
    ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
