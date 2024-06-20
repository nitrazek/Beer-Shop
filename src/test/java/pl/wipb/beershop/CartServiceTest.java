package pl.wipb.beershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.services.CartService;
import pl.wipb.beershop.utils.ProductToCart;
import pl.wipb.beershop.utils.RequestParsers;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import pl.wipb.beershop.models.utils.ProductCategory;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private AccountDao accountDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartProductDao cartProductDao;
    @Mock
    private RequestParsers parsers;
    @InjectMocks
    private CartService cartService;

    private Map<String, String[]> parameterMap;
    private Map<String, String> fieldToError;

    @BeforeEach
    void setUp() {
        parameterMap = new HashMap<>();
        fieldToError = new HashMap<>();
    }

    @Test
    void testCalculateTotalForCart() {
        Account account = new Account("user", "password", "email@test.com");
        Product product1 = new Product("product1", BigDecimal.valueOf(1.0));
        Product product2 = new Product("product2", BigDecimal.valueOf(2.0));
        List<CartProduct> cartProducts = Arrays.asList(
                new CartProduct(account, product1, 2),
                new CartProduct(account, product2)
        );
        
        assertEquals(new BigDecimal("4.0"), cartService.calculateTotalForCart(cartProducts));
    }
    
    @Test
    void testGetCartProductSize() {
        String login = "user";
        Account account = new Account(login, "password", "email@test.com");
        Product product1 = new Product("product1", BigDecimal.valueOf(10.0));
        Product product2 = new Product("product2", BigDecimal.valueOf(20.0));
        account.addCartProduct(new CartProduct(account, product1));
        account.addCartProduct(new CartProduct(account, product2));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(account));

        int result = cartService.getCartProductSize(login);

        assertEquals(2, result);
        verify(accountDao, times(1)).findByLogin(login);
    }

    @Test
    void testGetCartProductList() {
        String login = "user";
        Account account = new Account(login, "password", "email@test.com");
        Product product1 = new Product("product1", BigDecimal.valueOf(10.0));
        Product product2 = new Product("product2", BigDecimal.valueOf(20.0));
        account.addCartProduct(new CartProduct(account, product1));
        account.addCartProduct(new CartProduct(account, product2));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(account));

        List<CartProduct> result = cartService.getCartProductList(login);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0).getProduct());
        assertEquals(product2, result.get(1).getProduct());
        verify(accountDao, times(1)).findByLogin(login);
    }

    @Test
    void testAddProductToCart() {
        String login = "testUser";
        Long productId = 1L;
        int quantity = 2;
        Account account = new Account(login, "password", "email@test.com");
        Product product = new Product("product", BigDecimal.valueOf(10.0));
        when(parsers.parseAddEditProductToCartParams(parameterMap, fieldToError))
                .thenReturn(new ProductToCart(productId, quantity));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(account));
        when(productDao.findById(productId)).thenReturn(Optional.of(product));

        cartService.addProductToCart(login, parameterMap, fieldToError);

        assertEquals(1, account.getCartProducts().size());
        assertEquals(quantity, account.getCartProducts().get(0).getAmount());
        verify(accountDao, times(1)).findByLogin(login);
        verify(productDao, times(1)).findById(productId);
        verify(cartProductDao, times(1)).saveOrUpdate(any(CartProduct.class));
    }

    @Test
    void testEditProductAmountInCart() {
        String login = "testUser";
        Long productId = 1L;
        int newQuantity = 3;
        Account account = new Account(login, "password", "email@test.com");
        Product product = new Product("product", BigDecimal.valueOf(10.0));
        CartProduct cartProduct = new CartProduct(account, product, 2);
        account.addCartProduct(cartProduct);
        when(parsers.parseAddEditProductToCartParams(parameterMap, fieldToError))
                .thenReturn(new ProductToCart(productId, newQuantity));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(account));
        when(productDao.findById(productId)).thenReturn(Optional.of(product));

        cartService.editProductAmountInCart(login, parameterMap, fieldToError);

        assertEquals(newQuantity, cartProduct.getAmount());
        verify(accountDao, times(1)).findByLogin(login);
        verify(productDao, times(1)).findById(productId);
        verify(cartProductDao, times(1)).saveOrUpdate(any(CartProduct.class));
    }
}

