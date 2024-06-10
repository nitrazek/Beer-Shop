package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.models.Account;

import java.util.Optional;

@Stateless
public class JpaAccountDao extends JpaGenericDao<Account, Long> implements AccountDao {
    public Optional<Account> findByLogin(String login) {
        try {
            Account account = em.createNamedQuery("Account.findByLogin", Account.class)
                    .setParameter("login", login)
                    .getSingleResult();
            return Optional.of(account);
        } catch(NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Account> findByEmail(String email) {
        try {
            Account account = em.createNamedQuery("Account.findByEmail", Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(account);
        } catch(NoResultException ex) {
            return Optional.empty();
        }
    }
}
