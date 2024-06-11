package pl.wipb.beershop;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
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

        em.createQuery("DELETE FROM CartProduct").executeUpdate();
        log.debug("Cleared CART_PRODUCT table");

        em.createQuery("DELETE FROM OrderProduct").executeUpdate();
        log.debug("Cleared ORDER_PRODUCT table");

        em.createQuery("DELETE FROM Order").executeUpdate();
        log.debug("Cleared \"ORDER\" table");

        em.createQuery("DELETE FROM Account").executeUpdate();
        log.debug("Cleared ACCOUNT table");

        em.createQuery("DELETE FROM Product").executeUpdate();
        log.debug("Cleared PRODUCT table");

        Account account1 = new Account("admin", "admin", "admin@test.com", AccountRole.ADMIN);
        log.debug("Created account1: {}", account1.toString());

        Account account2 = new Account("dealer1", "dealer1", "dealer1@test.com", AccountRole.DEALER);
        log.debug("Created account2: {}", account2.toString());

        Account account3 = new Account("client1", "client1", "client1@test.com", AccountRole.CLIENT);
        log.debug("Created account3: {}", account3.toString());

        Account account4 = new Account("client2", "client2", "client2@test.com", AccountRole.CLIENT);
        log.debug("Created account4: {}", account4.toString());

        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
        em.persist(account4);

        Product product1 = new Product("Żywiec Jasne Pełne", ProductCategory.BEER, new BigDecimal("5.50"));
        log.debug("Created product1: {}", product1.toString());

        Product product2 = new Product("Tyskie Gronie", ProductCategory.BEER, new BigDecimal("4.80"));
        log.debug("Created product2: {}", product2.toString());

        Product product3 = new Product("Lech Premium", ProductCategory.BEER, new BigDecimal("4.90"));
        log.debug("Created product3: {}", product3.toString());

        Product product4 = new Product("Kuflowe Mocne", ProductCategory.BEER, new BigDecimal("5.30"));
        log.debug("Created product4: {}", product4.toString());

        Product product5 = new Product("Żubr", ProductCategory.BEER, new BigDecimal("4.70"));
        log.debug("Created product5: {}", product5.toString());

        em.persist(product1);
        em.persist(product2);
        em.persist(product3);
        em.persist(product4);
        em.persist(product5);

        CartProduct cartProduct1 = new CartProduct(account1, product1, 2);
        log.debug("Created cartProduct1: {}", cartProduct1.toString());

        CartProduct cartProduct2 = new CartProduct(account1, product2, 1);
        log.debug("Created cartProduct2: {}", cartProduct2.toString());

        CartProduct cartProduct3 = new CartProduct(account1, product3, 1);
        log.debug("Created cartProduct3: {}", cartProduct3.toString());

        em.persist(cartProduct1);
        em.persist(cartProduct2);
        em.persist(cartProduct3);

        log.debug("loadInitialData() finished");
    }
}
