package pl.wipb.beershop.utils;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;

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
}
