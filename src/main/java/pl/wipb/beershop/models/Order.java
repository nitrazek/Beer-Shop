package pl.wipb.beershop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "\"ORDER\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
