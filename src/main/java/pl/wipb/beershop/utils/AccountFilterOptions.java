package pl.wipb.beershop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountFilterOptions {
    private String login;
    private String email;
    private boolean adminRole;
    private boolean dealerRole;
    private boolean clientRole;
}
