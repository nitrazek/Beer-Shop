package pl.wipb.beershop.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import lombok.Data;
import pl.wipb.beershop.models.utils.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "\"ORDER\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @SequenceGenerator(name = "orderSeq", sequenceName = "ORDER_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CART;

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
}
