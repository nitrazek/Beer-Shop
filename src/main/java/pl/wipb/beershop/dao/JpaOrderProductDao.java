package pl.wipb.beershop.dao;

import pl.wipb.beershop.dao.interfaces.OrderProductDao;
import pl.wipb.beershop.models.OrderProduct;
import pl.wipb.beershop.models.utils.OrderProductId;

public class JpaOrderProductDao extends JpaGenericDao<OrderProduct, OrderProductId> implements OrderProductDao {
}
