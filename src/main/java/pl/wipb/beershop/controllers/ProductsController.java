package pl.wipb.beershop.controllers;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/shop/products")
public class ProductsController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private ProductsService prodService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        int cartProductSize = prodService.getCartProductSize(login);
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
        if (request.getParameter("filterButton") != null)
            doFilterRequest(request, response);
        else if (request.getParameter("addToCartButton") != null)
            doAddProductToCartRequest(request, response);
    }

    private void doFilterRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        List<Product> productList = prodService.getFilteredProductList(request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty() || productList == null) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("productList", request.getAttribute("productList"));
            request.setAttribute("cartProductSize", request.getAttribute("cartProductSize"));
            request.setAttribute("categoryList", request.getAttribute("categoryList"));

            request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
            return;
        }

        request.setAttribute("productList", productList);
        request.setAttribute("cartProductSize", request.getAttribute("cartProductSize"));
        request.setAttribute("categoryList", request.getAttribute("categoryList"));
        request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
    }

    private void doAddProductToCartRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        Map<String,String> fieldToError = new HashMap<>();
        prodService.addProductToCart(login, request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("productList", request.getAttribute("productList"));
            request.setAttribute("cartProductSize", request.getAttribute("cartProductSize"));
            request.setAttribute("categoryList", request.getAttribute("categoryList"));

            request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/shop/cart");
    }
}
