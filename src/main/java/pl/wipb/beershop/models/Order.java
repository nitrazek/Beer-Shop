package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;
import pl.wipb.beershop.models.utils.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "\"ORDER\"")
@NamedQueries({
        @NamedQuery(name = "Order.findByAccount", query = "SELECT o FROM Order o WHERE o.account = :accountId")
})
public class Order extends BaseModel<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @SequenceGenerator(name = "orderSeq", sequenceName = "ORDER_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private BigDecimal totalPrice = new BigDecimal("0.0");

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @PrePersist
    private void onCreate() {
        creationDate = LocalDateTime.now();
    }

    public Order() {}

    public Order(Account account) {
        this.account = account;
    }

    public void increaseTotalPrice(BigDecimal price) {
        totalPrice = totalPrice.add(price);
    }
}
