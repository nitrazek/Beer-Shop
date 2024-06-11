package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.dao.interfaces.OrderProductDao;
import pl.wipb.beershop.models.OrderProduct;
import pl.wipb.beershop.models.utils.OrderProductId;

@Stateless
public class JpaOrderProductDao extends JpaGenericDao<OrderProduct, OrderProductId> implements OrderProductDao {
}
