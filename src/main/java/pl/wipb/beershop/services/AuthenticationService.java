package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Order;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.utils.RequestParsers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Singleton
public class AuthenticationService {
    @EJB
    private AccountDao accountDao;
    @EJB
    private RequestParsers parsers;

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

    public Account handleRegister(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        Account account = parsers.parseRegisterParams(parameterMap, fieldToError);
        if (!fieldToError.isEmpty())
            return account;

        if (accountDao.findByLogin(account.getLogin()).isPresent()) {
            fieldToError.put("account", "Konto o podanym loginie już istnieje.");
            return account;
        }

        if (accountDao.findByEmail(account.getEmail()).isPresent()) {
            fieldToError.put("account", "Konto o podanym emailu już istnieje.");
            return account;
        }

        accountDao.saveOrUpdate(account);
        return account;
    }

    public boolean verifyAccount(String login) {
        if (login == null) {
            return false;
        }

        Optional<Account> account = accountDao.findByLogin(login);
        return account.isPresent();
    }
}
