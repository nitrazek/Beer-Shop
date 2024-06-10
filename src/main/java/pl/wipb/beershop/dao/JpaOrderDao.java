package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.models.Order;

@Stateless
public class JpaOrderDao extends JpaGenericDao<Order, Long> implements OrderDao {
}
