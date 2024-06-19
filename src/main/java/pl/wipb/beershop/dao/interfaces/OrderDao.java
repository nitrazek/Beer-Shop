package pl.wipb.beershop.dao.interfaces;

import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Order, Long> {
    List<Order> findByAccount(Account account);
}
