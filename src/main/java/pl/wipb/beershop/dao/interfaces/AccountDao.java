package pl.wipb.beershop.dao.interfaces;

import pl.wipb.beershop.models.Account;

import java.util.Optional;

public interface AccountDao extends GenericDao<Account, Long> {
    Optional<Account> findByLogin(String login);
    Optional<Account> findByEmail(String email);
}
