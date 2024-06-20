package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Product;

import java.util.List;

@Stateless
public class JpaProductDao extends JpaGenericDao<Product, Long> implements ProductDao {
  private static final Logger log = LogManager.getLogger();

  @Override
  public List<Product> findAll() {
    List<Product> products = super.findAll();
    return products;
  }
}
