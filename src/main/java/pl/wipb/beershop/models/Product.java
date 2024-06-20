package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import pl.wipb.beershop.models.utils.BaseModel;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;

@Data
@Entity
public class Product extends BaseModel<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSeq")
    @SequenceGenerator(name = "productSeq", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @ToString.Exclude
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public Product() {
        this(null, null, ProductCategory.BEER, null);
    }
    public Product(String name, BigDecimal price) {
        this(null, name, ProductCategory.BEER, price);
    }
    public Product(String name, ProductCategory category, BigDecimal price) {
        this(null, name, category, price);
    }
    public Product(Long id, String name, ProductCategory category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
