package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.utils.RequestParsers;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class ProductsService {
    private static final Logger log = LogManager.getLogger();

    @EJB
    private AccountDao accountDao;
    @EJB
    private ProductDao productDao;
    @EJB
    private RequestParsers parsers;

    public List<Product> getProductList() {
        List<Product> productList = productDao.findAll();
        log.debug("productList: {}", productList.toString());
        return productList;
    }

    public void addProductToCart(Account account, Product product, Integer amount) {

    }
}
