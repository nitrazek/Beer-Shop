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

    private void deleteExistingData() {
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
    }

    private void insertNewData(Object... objects) {
        for(Object object : objects) {
            log.debug("Created new {}: {}", object.getClass(), object.toString());
            em.persist(object);
        }
    }

    @PostConstruct
    public void loadInitialData() {
        log.debug("loadInitialData() started");

        deleteExistingData();
        insertNewData(
                new Account("admin", "admin", "admin@test.com", AccountRole.ADMIN),
                new Account("seller1", "seller1", "seller1@test.com", AccountRole.SELLER),
                new Account("client1", "client1", "client1@test.com", AccountRole.CLIENT),
                new Account("client2", "client2", "client2@test.com", AccountRole.CLIENT),
                new Product("Żywiec Męskie Spodnie", ProductCategory.BEER, new BigDecimal("5.50")),
                new Product("Tyskie Grochy", ProductCategory.BEER, new BigDecimal("4.80")),
                new Product("Lech Wodospad", ProductCategory.BEER, new BigDecimal("4.90")),
                new Product("Piwo bezalkoholowe w Gumiakach", ProductCategory.BEER, new BigDecimal("6.90")),
                new Product("Kuflowe Mega Mocne", ProductCategory.BEER, new BigDecimal("5.30")),
                new Product("Żubr na Hulajnodze", ProductCategory.BEER, new BigDecimal("4.70")),
                new Product("Kufel Świętego Graala", ProductCategory.BEER_GLASSWARE, new BigDecimal("25.99")),
                new Product("Szklanka z Magnesem na Lodówkę", ProductCategory.BEER_GLASSWARE, new BigDecimal("19.99")),
                new Product("Pokal z Wąsami", ProductCategory.BEER_GLASSWARE, new BigDecimal("22.49")),
                new Product("Fermentor w Kształcie Słonia", ProductCategory.HOMEBREWING_EQUIPMENT, new BigDecimal("149.99")),
                new Product("Kocioł do Gotowania Eliksirów", ProductCategory.HOMEBREWING_EQUIPMENT, new BigDecimal("249.99")),
                new Product("Zestaw do Chmielenia Jabłek", ProductCategory.HOMEBREWING_EQUIPMENT, new BigDecimal("79.99")),
                new Product("Otwieracz od Babci Halinki", ProductCategory.BEER_ACCESSORY, new BigDecimal("15.99")),
                new Product("Podkładka pod Piwo z Jednorożcem", ProductCategory.BEER_ACCESSORY, new BigDecimal("9.99")),
                new Product("Kapslownica z Kluczami od Domu", ProductCategory.BEER_ACCESSORY, new BigDecimal("45.99")),
                new Product("Miarka do Piwa z Cytryną", ProductCategory.BEER_ACCESSORY, new BigDecimal("12.99")),
                new Product("Koszulka z Rysunkiem Kota", ProductCategory.BEER_MERCHANDISE, new BigDecimal("29.99")),
                new Product("Naklejki na Butelki z Alienem", ProductCategory.BEER_MERCHANDISE, new BigDecimal("7.99")),
                new Product("Kufel z Włosami na Klacie", ProductCategory.BEER_MERCHANDISE, new BigDecimal("19.99")),
                new Product("Brelok do Kluczy z Pikachu", ProductCategory.BEER_MERCHANDISE, new BigDecimal("5.99")),
                new Product("Kwiaty piwonie", ProductCategory.BEER_MERCHANDISE, new BigDecimal("10.20"))
        );

        log.debug("loadInitialData() finished");
    }
}
