package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.servlet.http.HttpSession;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.CartProductId;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.utils.FilterOptions;
import pl.wipb.beershop.utils.ProductToCart;
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
    private CartProductDao cartProductDao;
    @EJB
    private RequestParsers parsers;

    public List<Product> getProductList() {
        return productDao.findAll();
    }

    public ProductCategory[] getCategoryList() {
        return ProductCategory.values();
    }

    public int getCartProductSize(String login) {
        Optional<Account> optAccountFromDb = accountDao.findByLogin(login);
        return optAccountFromDb.map(account -> account.getCartProducts().stream()
                .mapToInt(CartProduct::getAmount)
                .sum()).orElse(-1);
    }

    public List<Product> getFilteredProductList(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        FilterOptions filterOptions = parsers.parseFilterParams(parameterMap, fieldToError);
        if (!fieldToError.isEmpty())
            return null;

        List<Product> originalProductList = getProductList();

        log.debug(filterOptions.toString());

        return originalProductList.stream()
                .filter(p -> p.getPrice().compareTo(filterOptions.getMinPrice()) > 0 &&
                        p.getPrice().compareTo(filterOptions.getMaxPrice()) < 0 &&
                        p.getName().contains(filterOptions.getContains()) &&
                        p.getCategory().equals(filterOptions.getCategory()))
                .collect(Collectors.toList());
    }

    public void addProductToCart(String login, Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        ProductToCart productToCart = parsers.parseAddProductToCartParams(parameterMap, fieldToError);

        if (login == null) {
            fieldToError.put("param", "Użytkownik jest niezalogowany");
            return;
        }
        Optional<Account> optAccount = accountDao.findByLogin(login);
        if (optAccount.isEmpty()) return;
        Account account = optAccount.get();

        Optional<Product> optProduct = productDao.findById(productToCart.getProductId());
        if (optProduct.isEmpty()) return;
        Product product = optProduct.get();

        List<CartProduct> cartProducts = account.getCartProducts();
        CartProduct optCartProduct = cartProducts.stream()
                .filter(c -> c.getProduct().getId().equals(product.getId()) &&
                        c.getAccount().getId().equals(account.getId()))
                .findFirst()
                .orElse(new CartProduct(account, product, 0));
        optCartProduct.setAmount(optCartProduct.getAmount() + productToCart.getQuantity());
        account.getCartProducts().add(optCartProduct);

        cartProductDao.saveOrUpdate(optCartProduct);
        accountDao.saveOrUpdate(account);
    }
}
