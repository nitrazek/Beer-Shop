package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import pl.wipb.beershop.models.utils.OrderProductId;

@Data
@Entity
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    private Integer amount;
}
