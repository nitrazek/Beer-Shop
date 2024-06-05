package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Product;

import java.util.List;

@Stateless
public class JpaProductDao extends JpaGenericDao<Product, Long> implements ProductDao {
}
