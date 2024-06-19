package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Order;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.utils.AccountFilterOptions;
import pl.wipb.beershop.utils.RequestParsers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AuthenticationService {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private AccountDao accountDao;
    @EJB
    private RequestParsers parsers;

    public Account getAccount(Long id) {
        return accountDao.findById(id).orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountDao.findAll(); // our application is very safe, for real
    }

    public List<Account> getFilteredAccountList(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        AccountFilterOptions filterOptions = parsers.parseAccountFilterParams(parameterMap, fieldToError);
        if(!fieldToError.isEmpty())
            return null;

        List<Account> originalAccountList = getAllAccounts();

        return originalAccountList.stream().filter(a ->
                a.getLogin().contains(filterOptions.getLogin()) &&
                a.getEmail().contains(filterOptions.getEmail()) &&
                (!filterOptions.isClientRole() || a.getRole().compareTo(AccountRole.CLIENT) == 0) &&
                (!filterOptions.isDealerRole() || a.getRole().compareTo(AccountRole.DEALER) == 0) &&
                (!filterOptions.isAdminRole() || a.getRole().compareTo(AccountRole.ADMIN) == 0))
                .collect(Collectors.toList());
    }

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

    public boolean verifySeller(String login) {
        if (login == null) {
            return false;
        }

        Optional<Account> account = accountDao.findByLogin(login);
        return account.map(value -> value.getRole().equals(AccountRole.DEALER)).orElse(false);

    }

    public boolean verifyAdmin(String login) {
        if (login == null) {
            return false;
        }

        Optional<Account> account = accountDao.findByLogin(login);
        return account.map(value -> value.getRole().equals(AccountRole.ADMIN)).orElse(false);
    }
}
