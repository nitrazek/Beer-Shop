package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
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
    @EJB
    private AccountDao accountDao;
    @EJB
    private ProductDao productDao;
    @EJB
    private RequestParsers parsers;

    public List<Product> getProductList() {
        //return productDao.findAll();
        Product product1 = new Product("piwko1", ProductCategory.BEER, new BigDecimal("5.0"));
        Product product2 = new Product("piwko2", ProductCategory.BEER, new BigDecimal("4.0"));
        Product product3 = new Product("piwko3", ProductCategory.BEER, new BigDecimal("6.0"));
        return List.of(product1, product2, product3);
    }

    public void addProductToCart(Account account, Product product, Integer amount) {

    }
}
