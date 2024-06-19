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

    private Account createTestAccount(Long id, String login) {
        Account account = new Account();
        account.setId(id);
        account.setLogin(login);
        account.setCartProducts(new ArrayList<>()); // Inicjalizacja pustej listy produktów w koszyku
        return account;
    }

    private Product createTestProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    private CartProduct createTestCartProduct(Account account, Product product, int amount) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setAccount(account);
        cartProduct.setProduct(product);
        cartProduct.setAmount(amount);
        return cartProduct;
    }

    private Map<String, String[]> parameterMap;
    private Map<String, String> fieldToError;

    @BeforeEach
    void setUp() {
        parameterMap = new HashMap<>();
        fieldToError = new HashMap<>();
    }

    @Test
    void testGetCartProductSize_AccountExistsWithProducts() {
        String login = "testUser";
        Account testAccount = createTestAccount(1L, login);
        Product product1 = createTestProduct(1L, "Product 1", BigDecimal.valueOf(10.0));
        Product product2 = createTestProduct(2L, "Product 2", BigDecimal.valueOf(20.0));
        testAccount.addCartProduct(createTestCartProduct(testAccount, product1, 2));
        testAccount.addCartProduct(createTestCartProduct(testAccount, product2, 1));

        when(accountDao.findByLogin(login)).thenReturn(Optional.of(testAccount));

        int result = cartService.getCartProductSize(login);

        assertEquals(3, result);
        verify(accountDao, times(1)).findByLogin(login);
    }

    @Test
    void testGetCartProductSize_AccountDoesNotExist() {
        String login = "nonExistingUser";
        when(accountDao.findByLogin(login)).thenReturn(Optional.empty());

        int result = cartService.getCartProductSize(login);

        assertEquals(-1, result);
        verify(accountDao, times(1)).findByLogin(login);
    }

    @Test
    void testGetCartProductList_AccountExistsWithProducts() {
        String login = "testUser";
        Account testAccount = createTestAccount(1L, login);
        Product product1 = createTestProduct(1L, "Product 1", BigDecimal.valueOf(10.0));
        Product product2 = createTestProduct(2L, "Product 2", BigDecimal.valueOf(20.0));
        testAccount.addCartProduct(createTestCartProduct(testAccount, product1, 2));
        testAccount.addCartProduct(createTestCartProduct(testAccount, product2, 1));

        when(accountDao.findByLogin(login)).thenReturn(Optional.of(testAccount));

        List<CartProduct> result = cartService.getCartProductList(login);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0).getProduct());
        assertEquals(product2, result.get(1).getProduct());
        verify(accountDao, times(1)).findByLogin(login);
    }

    @Test
    void testGetCartProductList_AccountDoesNotExist() {
        String login = "nonExistingUser";
        when(accountDao.findByLogin(login)).thenReturn(Optional.empty());

        List<CartProduct> result = cartService.getCartProductList(login);

        assertNull(result);
        verify(accountDao, times(1)).findByLogin(login);
    }

    @Test
    void testAddProductToCart_Successful() {
        String login = "testUser";
        Long productId = 1L;
        int quantity = 2;
        Account testAccount = createTestAccount(1L, login);
        Product testProduct = createTestProduct(productId, "Product 1", BigDecimal.valueOf(10.0));

        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("productId", new String[]{String.valueOf(productId)});
        parameterMap.put("quantity", new String[]{String.valueOf(quantity)});
        Map<String, String> fieldToError = new HashMap<>();

        when(parsers.parseAddEditProductToCartParams(parameterMap, fieldToError))
                .thenReturn(new ProductToCart(productId, quantity));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(testAccount));
        when(productDao.findById(productId)).thenReturn(Optional.of(testProduct));
        when(cartProductDao.saveOrUpdate(any(CartProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.addProductToCart(login, parameterMap, fieldToError);

        assertEquals(1, testAccount.getCartProducts().size());
        assertEquals(quantity, testAccount.getCartProducts().get(0).getAmount());
        verify(accountDao, times(1)).findByLogin(login);
        verify(productDao, times(1)).findById(productId);
        verify(cartProductDao, times(1)).saveOrUpdate(any(CartProduct.class));
    }

    @Test
    void testAddProductToCart_UserNotLoggedIn() {
        String login = null;
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("productId", new String[]{"1"});
        parameterMap.put("quantity", new String[]{"2"});
        Map<String, String> fieldToError = new HashMap<>();

        cartService.addProductToCart(login, parameterMap, fieldToError);

        assertTrue(fieldToError.containsKey("param"));
        assertEquals("Użytkownik jest niezalogowany", fieldToError.get("param"));
        verify(accountDao, never()).findByLogin(anyString());
        verify(productDao, never()).findById(anyLong());
        verify(cartProductDao, never()).saveOrUpdate(any(CartProduct.class));
    }

    @Test
    void testAddProductToCart_ProductNotFound() {
        String login = "testUser";
        Long productId = 1L;
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("productId", new String[]{String.valueOf(productId)});
        parameterMap.put("quantity", new String[]{"2"});
        Map<String, String> fieldToError = new HashMap<>();

        when(parsers.parseAddEditProductToCartParams(parameterMap, fieldToError))
                .thenReturn(new ProductToCart(productId, 2));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(createTestAccount(1L, login)));
        when(productDao.findById(productId)).thenReturn(Optional.empty());

        cartService.addProductToCart(login, parameterMap, fieldToError);

        assertTrue(fieldToError.containsKey("param"));
        assertEquals("Wybrany produkt nie istnieje", fieldToError.get("param"));
        verify(accountDao, times(1)).findByLogin(login);
        verify(productDao, times(1)).findById(productId);
        verify(cartProductDao, never()).saveOrUpdate(any(CartProduct.class));
    }

    @Test
    void testEditProductAmountInCart_Successful() {
        String login = "testUser";
        Long productId = 1L;
        int newQuantity = 3;
        Account testAccount = createTestAccount(1L, login);
        Product testProduct = createTestProduct(productId, "Product 1", BigDecimal.valueOf(10.0));
        CartProduct cartProduct = createTestCartProduct(testAccount, testProduct, 2);
        testAccount.addCartProduct(cartProduct);

        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("productId", new String[]{String.valueOf(productId)});
        parameterMap.put("quantity", new String[]{String.valueOf(newQuantity)});
        Map<String, String> fieldToError = new HashMap<>();

        when(parsers.parseAddEditProductToCartParams(parameterMap, fieldToError))
                .thenReturn(new ProductToCart(productId, newQuantity));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(testAccount));
        when(productDao.findById(productId)).thenReturn(Optional.of(testProduct));
        when(cartProductDao.saveOrUpdate(any(CartProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.editProductAmountInCart(login, parameterMap, fieldToError);

        assertEquals(newQuantity, cartProduct.getAmount());
        verify(accountDao, times(1)).findByLogin(login);
        verify(productDao, times(1)).findById(productId);
        verify(cartProductDao, times(1)).saveOrUpdate(any(CartProduct.class));
    }
}

