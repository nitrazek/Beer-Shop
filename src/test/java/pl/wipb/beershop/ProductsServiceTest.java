package pl.wipb.beershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wipb.beershop.dao.interfaces.ProductDao;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.services.ProductsService;
import pl.wipb.beershop.utils.ProductFilterOptions;
import pl.wipb.beershop.utils.RequestParsers;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductsServiceTest {
    @Mock
    private ProductDao productDao;
    @Mock
    private RequestParsers parsers;
    @InjectMocks
    private ProductsService productsService;

    private Map<String, String[]> parameterMap;
    private Map<String, String> fieldToError;

    @BeforeEach
    void setUp() {
        parameterMap = new HashMap<>();
        fieldToError = new HashMap<>();
    }

    @Test
    void testGetProductList() {
        List<Product> productList = Arrays.asList(
                new Product("product1", ProductCategory.BEER, new BigDecimal("3.99")),
                new Product("product2", ProductCategory.BEER, new BigDecimal("4.49"))
        );
        when(productDao.findAll()).thenReturn(productList);

        List<Product> result = productsService.getProductList();

        assertEquals(2, result.size());
        assertEquals("product1", result.get(0).getName());
        assertEquals("product2", result.get(1).getName());
    }

    @Test
    void testGetCategoryList() {
        ProductCategory[] categories = productsService.getCategoryList();

        assertEquals(5, categories.length);
        assertEquals(ProductCategory.BEER, categories[0]);
        assertEquals(ProductCategory.BEER_GLASSWARE, categories[1]);
        assertEquals(ProductCategory.HOMEBREWING_EQUIPMENT, categories[2]);
        assertEquals(ProductCategory.BEER_ACCESSORY, categories[3]);
        assertEquals(ProductCategory.BEER_MERCHANDISE, categories[4]);
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = new Product("product1", ProductCategory.BEER, new BigDecimal("2.99"));
        when(productDao.findById(productId)).thenReturn(Optional.of(product));

        Product result = productsService.getProductById(productId);

        assertNotNull(result);
        assertEquals("product1", result.getName());
    }

    @Test
    void testGetFilteredProductList() {
        List<Product> productList = Arrays.asList(
                new Product("Piwo Jasne", ProductCategory.BEER, new BigDecimal("3.99")),
                new Product("Kufel do piwa", ProductCategory.BEER_GLASSWARE, new BigDecimal("12.99"))
        );
        ProductFilterOptions filterOptions = new ProductFilterOptions(
                new BigDecimal("2.00"),
                new BigDecimal("4.00"),
                "Piwo",
                ProductCategory.BEER
        );
        when(parsers.parseProductFilterParams(parameterMap, fieldToError)).thenReturn(filterOptions);
        when(productDao.findAll()).thenReturn(productList);

        List<Product> result = productsService.getFilteredProductList(parameterMap, fieldToError);

        assertEquals(1, result.size());
        assertEquals("Piwo Jasne", result.get(0).getName());
    }

    @Test
    void testAddOrEditProduct() {
        Product product = new Product("product", ProductCategory.BEER, new BigDecimal("5.99"));
        when(parsers.parseAddEditProductParams(parameterMap, fieldToError)).thenReturn(product);

        productsService.addOrEditProduct(parameterMap, fieldToError);

        verify(productDao, times(1)).saveOrUpdate(product);
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        Product product = new Product("product", ProductCategory.BEER, new BigDecimal("1.99"));
        when(productDao.findById(productId)).thenReturn(Optional.of(product));

        productsService.deleteProduct(productId, fieldToError);

        verify(productDao, times(1)).delete(product);
    }

    @Test
    void testDeleteProductNotExist() {
        Long productId = 1L;
        Product product = new Product("product", ProductCategory.BEER, new BigDecimal("1.99"));
        when(productDao.findById(productId)).thenReturn(Optional.empty());

        productsService.deleteProduct(productId, fieldToError);

        assertEquals("Produkt o podanym id nie istnieje.", fieldToError.get("param"));
    }
}
