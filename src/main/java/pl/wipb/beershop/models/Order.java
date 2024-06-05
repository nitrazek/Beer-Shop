package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account client;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @PrePersist
    private void onCreate() {
        creationDate = LocalDateTime.now();
    }
}
