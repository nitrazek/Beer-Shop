package pl.wipb.beershop;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;

@Singleton
@Startup
public class InitialDataLoader {
    @PersistenceContext(name = "PU")
    private EntityManager em;

    @PostConstruct
    public void loadInitialData() {
        Account account1 = new Account("admin", "admin", "email1", AccountRole.ADMIN);
        Account account2 = new Account("user2", "password2", "email2", AccountRole.DEALER);
        Account account3 = new Account("user1", "password1", "email1", AccountRole.CLIENT);

        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
    }
}
