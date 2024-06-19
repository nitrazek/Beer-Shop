package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.models.*;

import java.math.BigDecimal;
import java.util.List;
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
    private MailService mailService;

    public List<Order> getOrders(String login) {
        Optional<Account> optAccount = accountDao.findByLogin(login);
        if(optAccount.isEmpty()) return null;
        Account account = optAccount.get();

        return orderDao.findByAccount(account);
    }

    public void submitOrder(String login) {
        Optional<Account> optAccount = accountDao.findByLogin(login);
        if(optAccount.isEmpty()) return;
        Account account = optAccount.get();

        List<CartProduct> cartProducts = account.getCartProducts();
        Order order = new Order(account);
        cartProducts.forEach(cartProduct -> {
            Product product = cartProduct.getProduct();
            BigDecimal productPrice = product.getPrice();
            Integer productAmount = cartProduct.getAmount();

            order.getOrderProducts().add(new OrderProduct(order, product, productAmount));
            order.increaseTotalPrice(productPrice.multiply(BigDecimal.valueOf(productAmount)));

            cartProductDao.delete(cartProduct);
        });

        orderDao.saveOrUpdate(order);
        mailService.sendOrderSubmitMail(account.getEmail(), order);
    }
}
