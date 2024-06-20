package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.utils.AccountFilterOptions;
import pl.wipb.beershop.utils.RequestParsers;

@Singleton
public class AccountService {
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
        List<AccountRole> selectedRoles = new ArrayList<>();
        if (filterOptions.isClientRole())
            selectedRoles.add(AccountRole.CLIENT);
        if (filterOptions.isSellerRole())
            selectedRoles.add(AccountRole.SELLER);
        if (filterOptions.isAdminRole())
            selectedRoles.add(AccountRole.ADMIN);

        return originalAccountList.stream().filter(a ->
                        a.getLogin().contains(filterOptions.getLogin()) &&
                        a.getEmail().contains(filterOptions.getEmail()) &&
                        (selectedRoles.isEmpty() || selectedRoles.contains(a.getRole())))
                    .collect(Collectors.toList());
    }

    public void addOrEditAccount(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        Account account = parsers.parseAddEditAccountParams(parameterMap, fieldToError);
        if (!fieldToError.isEmpty())
            return;

        if (account.getPassword().isEmpty() && account.getId() != null) {
            Optional<Account> accountFromDb = accountDao.findById(account.getId());
            if(accountFromDb.isEmpty()) {
                fieldToError.put("id", "Konto o podanym id nie istnieje.");
                return;
            }
            account.setPassword(accountFromDb.get().getPassword());
        }

        accountDao.saveOrUpdate(account);
    }

    public void deleteAccount(Long accountId, Map<String,String> fieldToError) {
        Optional<Account> optAccount = accountDao.findById(accountId);
        if(optAccount.isEmpty()) {
            fieldToError.put("param", "Account o podanym id nie istnieje.");
            return;
        }

        accountDao.delete(optAccount.get());
    }
}
