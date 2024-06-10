package pl.wipb.beershop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FilterOptions {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String contains;
    private ProductCategory category;
}
