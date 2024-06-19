package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import pl.wipb.beershop.models.utils.BaseModel;
import pl.wipb.beershop.models.utils.CartProductId;

@Data
@Entity
@Table(name = "CART_PRODUCT")
public class CartProduct extends BaseModel<CartProductId> {
    @EmbeddedId
    private CartProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(nullable = false)
    private Integer amount;

    public CartProduct() {}

    public CartProduct(Account account, Product product) { this(account, product, 1); }

    public CartProduct(Account account, Product product, Integer amount) {
        this.account = account;
        this.product = product;
        this.amount = amount;
    }

    public void increaseAmount(Integer amount) {
        this.amount += amount;
    }
    
    public void decreaseAmount(Integer amount) {
        this.amount -= amount;
    }
}
