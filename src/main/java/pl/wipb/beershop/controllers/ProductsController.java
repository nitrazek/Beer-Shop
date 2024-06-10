package pl.wipb.beershop.controllers;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/shop/products")
public class ProductsController extends HttpServlet {
    private final Logger log = Logger.getLogger(LoginController.class.getName());
    @EJB
    private ProductsService prodService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> productList = prodService.getProductList();
        request.setAttribute("productList", productList);
        request.getRequestDispatcher("/WEB-INF/views/shop/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
