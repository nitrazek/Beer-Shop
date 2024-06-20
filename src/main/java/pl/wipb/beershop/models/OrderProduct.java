package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import pl.wipb.beershop.models.utils.BaseModel;
import pl.wipb.beershop.models.utils.OrderProductId;

@Data
@Entity
@Table(name = "ORDER_PRODUCT")
public class OrderProduct extends BaseModel<OrderProductId> {
    @EmbeddedId
    private OrderProductId id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(nullable = false)
    private Integer amount;

    public OrderProduct() {}
    public OrderProduct(Order order, Product product, Integer amount) {
        this.order = order;
        this.product = product;
        this.amount = amount;
    }
}
