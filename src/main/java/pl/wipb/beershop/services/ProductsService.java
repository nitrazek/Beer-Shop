package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.servlet.http.HttpSession;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.utils.FilterOptions;
import pl.wipb.beershop.utils.RequestParsers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Product product4 = new Product("piwko4", ProductCategory.BEER, new BigDecimal("3.59"));
        Product product5 = new Product("piwko5", ProductCategory.BEER, new BigDecimal("4.20"));
        Product product6 = new Product("piwko6", ProductCategory.BEER, new BigDecimal("5.67"));
        Product product7 = new Product("piwko7", ProductCategory.BEER, new BigDecimal("6.69"));
        return List.of(product1, product2, product3, product4, product5, product6, product7);
    }

    public ProductCategory[] getCategoryList() {
        return ProductCategory.values();
    }

    public int getCartProductSize(HttpSession session) {
        return 2137;
        /* Czeka na bazę
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        if (login == null) {
            return -1;
        }

        Optional<Account> optAccountFromDb = accountDao.findByLogin(login);
        return optAccountFromDb.map(account -> account.getCartProducts().size()).orElse(-1);*/
    }

    public List<Product> getFilteredProductList(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        FilterOptions filterOptions = parsers.parseFilterParams(parameterMap, fieldToError);
        if (!fieldToError.isEmpty())
            return null;

        List<Product> originalProductList = getProductList();

        return originalProductList.stream()
                .filter(p -> p.getPrice().compareTo(filterOptions.getMinPrice()) > 0)
                .filter(p -> p.getPrice().compareTo(filterOptions.getMaxPrice()) < 0)
                .filter(p -> p.getName().contains(filterOptions.getContains()))
                .filter(p -> p.getCategory().equals(filterOptions.getCategory()))
                .collect(Collectors.toList());
    }
}
