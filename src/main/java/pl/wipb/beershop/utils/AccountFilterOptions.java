package pl.wipb.beershop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountFilterOptions {
    private String login;
    private String email;
    private boolean adminRole;
    private boolean sellerRole;
    private boolean clientRole;
}
