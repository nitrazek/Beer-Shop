package pl.wipb.beershop;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.models.utils.ProductCategory;

import java.math.BigDecimal;

@Singleton
@Startup
public class InitialDataLoader {
    private static final Logger log = LogManager.getLogger();

    @PersistenceContext(name = "PU")
    private EntityManager em;

    @PostConstruct
    public void loadInitialData() {
        log.debug("loadInitialData() started");

        em.createNamedQuery("Account.deleteAll", Account.class).executeUpdate();
        em.createNamedQuery("Product.deleteAll", Product.class).executeUpdate();

        Account account1 = new Account("admin", "admin", "email1", AccountRole.ADMIN);
        Account account2 = new Account("user2", "password2", "email2", AccountRole.DEALER);
        Account account3 = new Account("user1", "password1", "email3", AccountRole.CLIENT);
        em.persist(account1);
        em.persist(account2);
        em.persist(account3);

        Product product1 = new Product("piwko1", ProductCategory.BEER, 101.51);
        Product product2 = new Product("piwko2", ProductCategory.BEER, 182.52);
        Product product3 = new Product("piwko3", ProductCategory.BEER, 234.53);
        em.persist(product1);
        em.persist(product2);
        em.persist(product3);

        log.debug("loadInitialData() finished");
    }
}
