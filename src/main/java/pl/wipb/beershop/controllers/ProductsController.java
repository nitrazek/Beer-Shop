package pl.wipb.beershop.controllers;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.services.AuthenticationService;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/shop/products")
public class ProductsController extends HttpServlet {
    private final Logger log = Logger.getLogger(LoginController.class.getName());
    @EJB
    private ProductsService prodService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cartProductSize = prodService.getCartProductSize(request.getSession(false));
        if(cartProductSize < 0) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        List<Product> productList = prodService.getProductList();
        ProductCategory[] categoryList = prodService.getCategoryList();

        request.setAttribute("productList", productList);
        request.setAttribute("cartProductSize", cartProductSize);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        List<Product> productList = prodService.getFilteredProductList(request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty() || productList == null) {
            request.setAttribute("errors", fieldToError);
            request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
            return;
        }

        request.setAttribute("productList", productList);
        request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
    }
}
