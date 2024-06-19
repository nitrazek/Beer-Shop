package pl.wipb.beershop.utils;

import jakarta.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Stateless
public class RequestParsers {
    private static final Logger log = LogManager.getLogger();

    private String getFirstValueByKey(Map<String, String[]> map, String key) {
        return Optional.ofNullable(map.get(key))
                .filter(values -> values.length > 0 && !values[0].isEmpty())
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

    public AccountFilterOptions parseAccountFilterParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String login = getFirstValueByKey(paramToValue, "accountLogin");
        String email = getFirstValueByKey(paramToValue, "accountEmail");
        boolean adminRole = getFirstValueByKey(paramToValue, "adminRole") != null;
        boolean sellerRole = getFirstValueByKey(paramToValue, "sellerRole") != null;
        boolean clientRole = getFirstValueByKey(paramToValue, "clientRole") != null;

        if (login == null) {
            login = "";
        }

        if (email == null) {
            email = "";
        }

        return new AccountFilterOptions(login, email, adminRole, sellerRole, clientRole);
    }

    public ProductFilterOptions parseProductFilterParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        BigDecimal minPrice = new BigDecimal(Optional.ofNullable(getFirstValueByKey(paramToValue, "minValue")).orElse("-1"));
        BigDecimal maxPrice = new BigDecimal(Optional.ofNullable(getFirstValueByKey(paramToValue, "maxValue")).orElse("-1"));
        String contains = getFirstValueByKey(paramToValue, "contains");
        String category = getFirstValueByKey(paramToValue, "category");

        if (minPrice.compareTo(maxPrice) > 0) {
            fieldToError.put("param", "Minimalna wartość nie może być większa od maksymalnej.");
            return null;
        }

        if (category != null && Arrays.stream(ProductCategory.values()).filter(c -> c.name().equals(category)).findAny().isEmpty()) {
            fieldToError.put("param", "Podana wartość \"category\" nie istnieje.");
            return null;
        }

        if (contains == null) {
            contains = "";
        }

        return new ProductFilterOptions(minPrice, maxPrice, contains.trim(), category != null ? ProductCategory.valueOf(category) : null);
    }

    public ProductToCart parseAddEditProductToCartParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        long productId = Long.parseLong(Optional.ofNullable(getFirstValueByKey(paramToValue, "productId")).orElse("-1"));
        int quantity = Integer.parseInt(Optional.ofNullable(getFirstValueByKey(paramToValue, "quantity")).orElse("-1"));

        if (productId < 0) {
            fieldToError.put("param", "Wartość \"productId\" musi być dodatnia.");
            return null;
        }

        if (quantity < 0) {
            fieldToError.put("param", "Wartość \"quantity\" musi być dodatnia.");
            return null;
        }

        return new ProductToCart(productId, quantity);
    }

    public Product parseAddEditProductParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String id = getFirstValueByKey(paramToValue, "productId");
        String name = getFirstValueByKey(paramToValue, "productName");
        String category = getFirstValueByKey(paramToValue, "productCategory");
        BigDecimal price = new BigDecimal(Optional.ofNullable(getFirstValueByKey(paramToValue, "productPrice")).orElse("-1"));

        if (id != null && !id.matches("^\\d+$")) {
            fieldToError.put("id", "Id produktu może być tylko numerem.");
        }

        if (name == null || name.isEmpty()) {
            fieldToError.put("name", "Pole \"name\" nie może być puste.");
        }

        if (category == null || Arrays.stream(ProductCategory.values()).filter(c -> c.name().equals(category)).findAny().isEmpty()) {
            fieldToError.put("category", "Wartość \"category\" musi istnieć.");
        }

        if (price.compareTo(new BigDecimal(0)) < 0) {
            fieldToError.put("price", "Wartość \"price\" jest nie prawidłowa.");
        }

        return fieldToError.isEmpty() ? new Product(id != null ? Long.parseLong(id): null, name, ProductCategory.valueOf(category), price) : null;
    }

    public Account parseAddEditAccountParams(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String id = getFirstValueByKey(paramToValue, "accountId");
        String login = getFirstValueByKey(paramToValue, "accountLogin");
        String email = getFirstValueByKey(paramToValue, "accountEmail");
        String password = getFirstValueByKey(paramToValue, "accountPassword");
        String retypedPassword = getFirstValueByKey(paramToValue, "accountRetypedPassword");
        String role = getFirstValueByKey(paramToValue, "accountRole");

        if (password == null) {
            password = "";
        }

        if (retypedPassword == null) {
            retypedPassword = "";
        }

        if (id != null && !id.matches("^\\d+$")) {
            fieldToError.put("id", "Id konta może być tylko numerem.");
        }

        if (login == null || login.trim().isEmpty()) {
            fieldToError.put("login", "Pole nazwa nie może być puste.");
        }

        if (!retypedPassword.trim().equals(password)) {
            fieldToError.put("password", "Hasła nie są takie same.");
        }

        if (email == null || email.trim().isEmpty()) {
            fieldToError.put("email", "Pole email nie może być puste.");
        }

        if (email != null && !email.trim().isEmpty() && !email.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            fieldToError.put("email", "Pole email jest w nie właściwym wzorze.");
        }

        if (role == null || Arrays.stream(AccountRole.values()).filter(a -> a.name().equals(role)).findAny().isEmpty()) {
            fieldToError.put("param", "Podana rola nie istnieje.");
        }

        return fieldToError.isEmpty() ? new Account(id != null ? Long.parseLong(id): null, login.trim(), password.trim(), email.trim(), AccountRole.valueOf(role)) : null;
    }
}
