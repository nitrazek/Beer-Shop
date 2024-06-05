package pl.wipb.beershop.utils;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.models.Account;

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

        return fieldToError.isEmpty() ? new Account(login, password) : null;
    }
}
