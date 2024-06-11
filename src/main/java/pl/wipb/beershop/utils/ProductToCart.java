package pl.wipb.beershop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductToCart {
    long productId;
    int quantity;
}
