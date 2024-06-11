package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger log = LogManager.getLogger();

    @EJB
    private AccountDao accountDao;
    @EJB
    private ProductDao productDao;
    @EJB
    private RequestParsers parsers;

    public List<Product> getProductList() {
        return productDao.findAll();
    }

    public ProductCategory[] getCategoryList() {
        return ProductCategory.values();
    }

    public int getCartProductSize(HttpSession session) {
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        if (login == null) {
            return -1;
        }

        Optional<Account> optAccountFromDb = accountDao.findByLogin(login);
        return optAccountFromDb.map(account -> account.getCartProducts().size()).orElse(-1);
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
