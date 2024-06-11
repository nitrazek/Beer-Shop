package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.utils.CartProductId;

@Stateless
public class JpaCartProductDao extends JpaGenericDao<CartProduct, CartProductId> implements CartProductDao {
}
