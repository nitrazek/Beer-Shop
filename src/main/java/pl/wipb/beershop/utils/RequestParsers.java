package pl.wipb.beershop.utils;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Stateless
public class RequestParsers {
    public Account parseLoginParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String login = paramToValue.get("login")[0];
        String password = paramToValue.get("password")[0];

        if (login == null || login.trim().isEmpty()) {
            fieldToError.put("login", "Pole nazwa nie może być puste.");
        }

        if (password == null || password.trim().isEmpty()) {
            fieldToError.put("password", "Pole hasło nie może być puste.");
        }

        return fieldToError.isEmpty() ? new Account(login.trim(), password.trim()) : null;
    }

    public Account parseRegisterParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String login = paramToValue.get("login")[0];
        String password = paramToValue.get("password")[0];
        String confirm_password = paramToValue.get("confirm_password")[0];
        String email = paramToValue.get("email")[0];

        if (login == null || login.trim().isEmpty()) {
            fieldToError.put("login", "Pole nazwa nie może być puste.");
        }

        if (password == null || password.trim().isEmpty()) {
            fieldToError.put("password", "Pole hasło nie może być puste.");
        }

        if (confirm_password == null || !confirm_password.trim().equals(password)) {
            fieldToError.put("password", "Hasła nie są takie same.");
        }

        if (email == null || email.trim().isEmpty()) {
            fieldToError.put("email", "Pole email nie może być puste.");
        }

        if (email != null && !email.trim().isEmpty() && !email.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            fieldToError.put("email", "Pole email jest w nie właściwym wzorze.");
        }

        return fieldToError.isEmpty() ? new Account(login.trim(), password.trim(), email.trim(), AccountRole.CLIENT) : null;
    }

    public FilterOptions parseFilterParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        BigDecimal minPrice = new BigDecimal(paramToValue.get("minValue")[0]);
        BigDecimal maxPrice = new BigDecimal(paramToValue.get("maxValue")[0]);
        String contains = paramToValue.get("contains")[0];
        String category = paramToValue.get("category")[0];

        if (minPrice.compareTo(new BigDecimal(0)) < 0) {
            fieldToError.put("param", "Wartość \"minValue\" jest nie prawidłowa.");
            return null;
        }

        if (maxPrice.compareTo(new BigDecimal(0)) < 0) {
            fieldToError.put("param", "Wartość \"maxValue\" jest nie prawidłowa.");
            return null;
        }

        if (contains == null) {
            fieldToError.put("param", "Wartość \"containString\" musi istnieć.");
            return null;
        }

        if (category == null || Arrays.stream(ProductCategory.values()).filter(c -> c.name().equals(category)).findAny().isEmpty()) {
            fieldToError.put("param", "Wartość \"category\" musi istnieć.");
            return null;
        }

        return new FilterOptions(minPrice, maxPrice, contains.trim(), ProductCategory.valueOf(category));
    }
}
