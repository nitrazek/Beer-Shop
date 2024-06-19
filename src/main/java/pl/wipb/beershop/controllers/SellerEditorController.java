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
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.models.utils.ProductCategory;
import pl.wipb.beershop.services.AccountService;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/seller/editor/*")
public class SellerEditorController extends HttpServlet {
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

        ProductCategory[] categoryList = prodService.getCategoryList();
        request.setAttribute("categoryList", categoryList);

        String productIdParam = request.getParameter("productId");
        if (productIdParam != null && productIdParam.matches("\\d+")) {
            Long productId = Long.parseLong(productIdParam);
            Product product = prodService.getProductById(productId);
            if(product != null)
                request.setAttribute("product", product);
        }

        request.getRequestDispatcher("/WEB-INF/views/seller-panel/product-editor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        prodService.addOrEditProduct(request.getParameterMap(), fieldToError);

        ProductCategory[] categoryList = prodService.getCategoryList();

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("categoryList", categoryList);

            String productIdParam = request.getParameter("productId");
            if (productIdParam != null && productIdParam.matches("\\d+")) {
                Long productId = Long.parseLong(productIdParam);
                Product product = prodService.getProductById(productId);
                if (product != null)
                    request.setAttribute("product", product);
            }

            request.getRequestDispatcher("/WEB-INF/views/seller-panel/product-editor.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/seller/products");
    }
}
