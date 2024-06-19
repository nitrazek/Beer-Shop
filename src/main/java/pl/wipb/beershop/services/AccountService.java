package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import java.util.Optional;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;

@Singleton
public class AccountService {
    @EJB
    private AccountDao accountDao;
  
    public void createOrUpdateAccount(String login, String password, String email, AccountRole role) {
        Account account = new Account(login, password, email, role);
        accountDao.saveOrUpdate(account);
    }

    public void deleteAccount(Long accountId) {
        Optional<Account> optAccount = accountDao.findById(accountId);
        if(optAccount.isEmpty()) return;
        accountDao.delete(optAccount.get());
    }
}
