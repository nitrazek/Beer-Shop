package pl.wipb.beershop.dao;

import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.utils.CartProductId;

public class JpaCartProductDao extends JpaGenericDao<CartProduct, CartProductId> implements CartProductDao {
}
