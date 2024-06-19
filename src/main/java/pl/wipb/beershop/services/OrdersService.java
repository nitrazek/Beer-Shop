package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.dao.interfaces.OrderProductDao;
import pl.wipb.beershop.models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class OrdersService {
    @EJB
    private AccountDao accountDao;
    @EJB
    private CartProductDao cartProductDao;
    @EJB
    private OrderDao orderDao;
    @EJB
    private OrderProductDao orderProductDao;
    @EJB
    private MailService mailService;

    public List<Order> getOrders(String login) {
        if (login == null) {
            return null;
        }
        Optional<Account> optAccount = accountDao.findByLogin(login);
        return optAccount.map(account -> orderDao.findByAccount(account)).orElse(null);

    }

    public void submitOrder(String login, Map<String,String> fieldToError) {
        if (login == null) {
            fieldToError.put("param", "Użytkownik jest niezalogowany.");
            return;
        }
        Optional<Account> optAccount = accountDao.findByLogin(login);
        if(optAccount.isEmpty()) {
            fieldToError.put("param", "Konto nie istnieje.");
            return;
        }
        Account account = optAccount.get();

        Order order = new Order(account);
        account.getCartProducts().forEach(cartProduct -> {
            Product product = cartProduct.getProduct();
            BigDecimal productPrice = product.getPrice();
            Integer productAmount = cartProduct.getAmount();

            OrderProduct orderProduct = new OrderProduct(order, product, productAmount);
            order.getOrderProducts().add(orderProduct);
            order.increaseTotalPrice(productPrice.multiply(BigDecimal.valueOf(productAmount)));
            orderProductDao.saveOrUpdate(orderProduct);
            cartProductDao.delete(cartProduct);
        });

        account.setCartProducts(new ArrayList<>());
        accountDao.saveOrUpdate(account);
        orderDao.saveOrUpdate(order);
        mailService.sendOrderSubmitMail(account.getEmail(), order);
    }
}
