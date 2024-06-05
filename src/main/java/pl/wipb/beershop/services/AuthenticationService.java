package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.utils.RequestParsers;

import java.util.Map;
import java.util.Optional;

@Singleton
public class AuthenticationService {
    @EJB
    AccountDao accountDao;

    @EJB
    RequestParsers parsers;

    public Account handleLogin(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        Account account = parsers.parseLoginParams(parameterMap, fieldToError);

        if (!fieldToError.isEmpty())
            return account;

        Optional<Account> optAccountFromDb = accountDao.findByLogin(account.getLogin());
        if (optAccountFromDb.isEmpty()) {
            fieldToError.put("login", "Konto o podanej nazwie nie istnieje.");
            return account;
        }

        Account accountFromDb = optAccountFromDb.get();
        if (!accountFromDb.getPassword().equals(account.getPassword())) {
            fieldToError.put("password", "Podano złe hasło.");
            return account;
        }

        return accountFromDb;
    }
}
