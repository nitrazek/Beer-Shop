package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;

@Data
@Entity
@NamedQueries({
  @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
  @NamedQuery(name = "Product.deleteAll", query = "DELETE FROM Product")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false, precision = 7, scale = 2)
    private Double price;

    public Product() {};

    public Product(String name, ProductCategory category, Double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
