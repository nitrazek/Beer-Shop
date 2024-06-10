package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import pl.wipb.beershop.models.utils.CartProductId;

@Data
@Entity
public class CartProduct {
    @EmbeddedId
    private CartProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(nullable = false)
    private Integer amount = 1;

    public CartProduct() {}

    public CartProduct(Account account, Product product, Integer amount) {
        this.account = account;
        this.product = product;
        this.amount = amount;
    }
}
