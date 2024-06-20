package pl.wipb.beershop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.dao.interfaces.OrderProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Order;
import pl.wipb.beershop.models.OrderProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.services.MailService;
import pl.wipb.beershop.services.OrdersService;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {
    @Mock
    private AccountDao accountDao;
    @Mock
    private CartProductDao cartProductDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderProductDao orderProductDao;
    @Mock
    private MailService mailService;
    @InjectMocks
    private OrdersService ordersService;

    private Map<String, String> fieldToError;
    
    @BeforeEach
    void setUp() {
        fieldToError = new HashMap<>();
    }

    @Test
    void testGetOrders() {
        String login = "user";
        Account account = new Account(login, "password", "email@test.com");
        Product product = new Product("product", BigDecimal.valueOf(1.0));
        Order order = new Order(account);
        order.getOrderProducts().add(new OrderProduct(order, product, 1));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(account));
        when(orderDao.findByAccount(account)).thenReturn(Arrays.asList(order));
        
        List<Order> orders = ordersService.getOrders(login);
        
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    void testSubmitOrder() {
        String login = "user";
        String email = "email@test.com";
        Account account = new Account(login, "password", email);
        Product product = new Product("product", BigDecimal.valueOf(1.0));
        account.addCartProduct(new CartProduct(account, product));
        when(accountDao.findByLogin(login)).thenReturn(Optional.of(account));
        
        ordersService.submitOrder(login, fieldToError);
        
        verify(orderProductDao, times(1)).saveOrUpdate(any(OrderProduct.class));
        verify(cartProductDao, times(1)).delete(any(CartProduct.class));
        verify(accountDao, times(1)).saveOrUpdate(account);
        verify(orderDao, times(1)).saveOrUpdate(any(Order.class));
        verify(mailService, times(1)).sendOrderSubmitMail(eq(email), any(Order.class));
        assertTrue(fieldToError.isEmpty());
    }
}
