package pl.wipb.beershop;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;

@Singleton
@Startup
public class InitialDataLoader {
    @PersistenceContext(name = "PU")
    private EntityManager em;

    @PostConstruct
    public void loadInitialData() {
        Account account1 = new Account("admin", "admin", "email1", AccountRole.ADMIN);
        Account account2 = new Account("user2", "password2", "email2", AccountRole.DEALER);
        Account account3 = new Account("user1", "password1", "email3", AccountRole.CLIENT);
        em.persist(account1);
        em.persist(account2);
        em.persist(account3);

        Product product1 = new Product("piwko1", ProductCategory.BEER, new BigDecimal("5.0"));
        Product product2 = new Product("piwko2", ProductCategory.BEER, new BigDecimal("4.0"));
        Product product3 = new Product("piwko3", ProductCategory.BEER, new BigDecimal("6.0"));
        em.persist(product1);
        em.persist(product2);
        em.persist(product3);
    }
}
