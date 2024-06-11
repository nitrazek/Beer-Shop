package pl.wipb.beershop.utils;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Stateless
public class RequestParsers {
    private String getFirstValueByKey(Map<String, String[]> map, String key) {
        return Optional.ofNullable(map.get(key))
                .filter(values -> values.length > 0)
                .map(values -> values[0])
                .orElse(null);
    }

    public Account parseLoginParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String login = getFirstValueByKey(paramToValue, "login");
        String password = getFirstValueByKey(paramToValue, "password");

        if (login == null || login.trim().isEmpty()) {
            fieldToError.put("login", "Pole nazwa nie może być puste.");
        }

        if (password == null || password.trim().isEmpty()) {
            fieldToError.put("password", "Pole hasło nie może być puste.");
        }

        return fieldToError.isEmpty() ? new Account(login.trim(), password.trim()) : null;
    }

    public Account parseRegisterParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String login = getFirstValueByKey(paramToValue, "login");
        String password = getFirstValueByKey(paramToValue, "password");
        String confirm_password = getFirstValueByKey(paramToValue, "confirm_password");
        String email = getFirstValueByKey(paramToValue, "email");

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
        BigDecimal minPrice = new BigDecimal(Optional.ofNullable(getFirstValueByKey(paramToValue, "minValue")).orElse("-1"));
        BigDecimal maxPrice = new BigDecimal(Optional.ofNullable(getFirstValueByKey(paramToValue, "maxValue")).orElse("-1"));
        String contains = getFirstValueByKey(paramToValue, "contains");
        String category = getFirstValueByKey(paramToValue, "category");

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

    public ProductToCart parseAddProductToCartParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        long productId = Long.parseLong(Optional.ofNullable(getFirstValueByKey(paramToValue, "productId")).orElse("-1"));
        int quantity = Integer.parseInt(Optional.ofNullable(getFirstValueByKey(paramToValue, "quantity")).orElse("-1"));

        if (productId < 0) {
            fieldToError.put("param", "Wartość \"productId\" musi być dodatnia");
            return null;
        }

        if (quantity < 0) {
            fieldToError.put("param", "Wartość \"quantity\" musi być dodatnia");
            return null;
        }

        return new ProductToCart(productId, quantity);
    }
}
