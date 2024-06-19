package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.utils.ProductToCart;
import pl.wipb.beershop.utils.RequestParsers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CartService {
    @EJB
    private AccountDao accountDao;
    @EJB
    private RequestParsers parsers;
    @EJB
    private ProductDao productDao;
    @EJB
    private CartProductDao cartProductDao;

    public BigDecimal calculateTotalForCart(List<CartProduct> cartProductList) {
        return cartProductList.stream()
                .map(cartProduct -> BigDecimal.valueOf(cartProduct.getAmount()).multiply(cartProduct.getProduct().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartProductSize(String login) {
        Optional<Account> optAccountFromDb = accountDao.findByLogin(login);
        return optAccountFromDb.map(account -> account.getCartProducts().stream()
                .mapToInt(CartProduct::getAmount)
                .sum()).orElse(-1);
    }

    public List<CartProduct> getCartProductList(String login) {
        Optional<Account> optAccountFromDb = accountDao.findByLogin(login);
        return optAccountFromDb.map(Account::getCartProducts).orElse(null);
    }

    public void addProductToCart(String login, Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        ProductToCart productToCart = parsers.parseAddEditProductToCartParams(parameterMap, fieldToError);
        if (productToCart == null)
            return;
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

        Optional<CartProduct> optCartProduct = account.getCartProducts().stream()
                .filter(cartProduct -> cartProduct.getAccount().equals(account) && cartProduct.getProduct().equals(product))
                .findFirst();

        if (optCartProduct.isEmpty()) {
            CartProduct newCartProduct = new CartProduct(account, product, productToCart.getQuantity());
            account.addCartProduct(newCartProduct);
            cartProductDao.saveOrUpdate(newCartProduct);
            accountDao.saveOrUpdate(account);
            return;
        }

        CartProduct cartProduct = optCartProduct.get();
        cartProduct.increaseAmount(productToCart.getQuantity());
        cartProductDao.saveOrUpdate(cartProduct);
        accountDao.saveOrUpdate(account);
    }

    public void editProductAmountInCart(String login, Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        ProductToCart productToCart = parsers.parseAddEditProductToCartParams(parameterMap, fieldToError);
        if (productToCart == null)
            return;
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

        Optional<CartProduct> optCartProduct = account.getCartProducts().stream()
                .filter(cartProduct -> cartProduct.getAccount().equals(account) && cartProduct.getProduct().equals(product))
                .findFirst();

        if (optCartProduct.isEmpty()) {
            fieldToError.put("param", "Wybranego produktu nie ma w koszyku");
            return;
        }

        CartProduct cartProduct = optCartProduct.get();
        cartProduct.setAmount(productToCart.getQuantity());
        cartProductDao.saveOrUpdate(cartProduct);
        accountDao.saveOrUpdate(account);
    }

    public void removeProductFromCart(String login, Long productId, Map<String,String> fieldToError) {
        if (login == null) {
            fieldToError.put("param", "Użytkownik jest niezalogowany");
            return;
        }

        Optional<Account> optAccount = accountDao.findByLogin(login);
        if(optAccount.isEmpty()) return;
        Account account = optAccount.get();

        Optional<Product> optProduct = productDao.findById(productId);
        if(optProduct.isEmpty()) return;
        Product product = optProduct.get();

        Optional<CartProduct> optCartProduct = account.getCartProducts().stream()
                .filter(cartProduct -> cartProduct.getAccount().equals(account) && cartProduct.getProduct().equals(product))
                .findFirst();

        if(optCartProduct.isEmpty()) {
            fieldToError.put("param", "Wybranego produktu nie ma w koszyku");
            return;
        }

        CartProduct cartProduct = optCartProduct.get();
        account.getCartProducts().remove(cartProduct);
        cartProductDao.delete(cartProduct);
        accountDao.saveOrUpdate(account);
    }
}
