package pl.wipb.beershop.models.utils;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CartProductId implements Serializable {
    private Long accountId;
    private Long productId;
}
