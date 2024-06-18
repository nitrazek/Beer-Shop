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
import pl.wipb.beershop.models.CartProduct;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.util.List;

@WebServlet("/shop/cart")
public class CartController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private ProductsService prodService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        List<CartProduct> cartProductList = prodService.getCartProductList(login);
        if (cartProductList == null) {
            response.sendRedirect(request.getContextPath() + "login");
            return;
        }

        request.setAttribute("cartProductList", cartProductList);
        request.getRequestDispatcher("/WEB-INF/views/shop/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
