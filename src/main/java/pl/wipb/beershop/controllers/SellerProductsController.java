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

@WebServlet("/seller/products")
public class SellerProductsController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private ProductsService prodService;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Product> productList = prodService.getProductList();
        ProductCategory[] categoryList = prodService.getCategoryList();

        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/views/seller-panel/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("filterButton") != null)
            doFilterRequest(request, response);
        else if (request.getParameter("deleteProductButton") != null)
            doDeleteProductButton(request, response);
    }

    private void doFilterRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        List<Product> productList = prodService.getFilteredProductList(request.getParameterMap(), fieldToError);
        ProductCategory[] categoryList = prodService.getCategoryList();

        if(!fieldToError.isEmpty() || productList == null) {
            productList = prodService.getProductList();
            request.setAttribute("errors", fieldToError);
            request.setAttribute("productList", productList);
            request.setAttribute("categoryList", categoryList);
            request.getRequestDispatcher("/WEB-INF/views/seller-panel/products.jsp").forward(request, response);
            return;
        }

        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/views/seller-panel/products.jsp").forward(request, response);
    }

    private void doDeleteProductButton(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();

        String productIdParam = request.getParameter("productId");
        if (productIdParam != null && productIdParam.matches("\\d+")) {
            Long productId = Long.parseLong(productIdParam);
            prodService.deleteProduct(productId, fieldToError);
        } else {
            fieldToError.put("param", "Id produktu jest nieprawidłowe.");
        }

        List<Product> productList = prodService.getProductList();
        ProductCategory[] categoryList = prodService.getCategoryList();

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("productList", productList);
            request.setAttribute("categoryList", categoryList);

            request.getRequestDispatcher("/WEB-INF/views/seller-panel/products.jsp").forward(request, response);
            return;
        }

        request.setAttribute("productList", productList);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/views/seller-panel/products.jsp").forward(request, response);
    }
}
