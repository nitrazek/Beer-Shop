package pl.wipb.beershop.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.dao.interfaces.CartProductDao;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.utils.ProductFilterOptions;
import pl.wipb.beershop.utils.ProductToCart;
import pl.wipb.beershop.utils.RequestParsers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ProductsService {
    private static final Logger log = LogManager.getLogger();

    @EJB
    private AccountDao accountDao;
    @EJB
    private ProductDao productDao;
    @EJB
    private CartProductDao cartProductDao;
    @EJB
    private RequestParsers parsers;

    public List<Product> getProductList() {
        return productDao.findAll();
    }

    public ProductCategory[] getCategoryList() {
        return ProductCategory.values();
    }

    public Product getProductById(Long productId) {
        return productDao.findById(productId).orElse(null);
    }

    public List<Product> getFilteredProductList(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        ProductFilterOptions filterOptions = parsers.parseProductFilterParams(parameterMap, fieldToError);
        if (!fieldToError.isEmpty())
            return null;

        List<Product> originalProductList = getProductList();

        return originalProductList.stream().filter(p ->
                (filterOptions.getMinPrice().compareTo(new BigDecimal(0)) < 0 || p.getPrice().compareTo(filterOptions.getMinPrice()) > 0) &&
                (filterOptions.getMaxPrice().compareTo(new BigDecimal(0)) < 0 || p.getPrice().compareTo(filterOptions.getMaxPrice()) < 0) &&
                (filterOptions.getCategory() == null || p.getCategory().equals(filterOptions.getCategory())) &&
                p.getName().contains(filterOptions.getContains()))
                .collect(Collectors.toList());
    }
    
    public void addOrEditProduct(Map<String, String[]> parameterMap, Map<String,String> fieldToError) {
        Product product = parsers.parseAddEditProductParams(parameterMap, fieldToError);
        if (!fieldToError.isEmpty())
            return;

        productDao.saveOrUpdate(product);
    }

    public void deleteProduct(Long productId, Map<String,String> fieldToError) {
        Optional<Product> optProduct = productDao.findById(productId);
        if(optProduct.isEmpty()) {
            fieldToError.put("param", "Produkt o podanym id nie istnieje.");
            return;
        }

        productDao.delete(optProduct.get());
    }
}
