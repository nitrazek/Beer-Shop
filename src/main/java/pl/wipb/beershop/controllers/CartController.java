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
import pl.wipb.beershop.models.Product;
import pl.wipb.beershop.services.CartService;
import pl.wipb.beershop.services.OrdersService;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/shop/cart")
public class CartController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private CartService cartService;
    @EJB
    private OrdersService ordersService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        List<CartProduct> cartProductList = cartService.getCartProductList(login);
        if (cartProductList == null) {
            response.sendRedirect(request.getContextPath() + "login");
            return;
        }
        BigDecimal totalForCart = cartService.calculateTotalForCart(cartProductList);

        request.setAttribute("totalPrice", totalForCart);
        request.setAttribute("cartProductList", cartProductList);
        request.getRequestDispatcher("/WEB-INF/views/shop/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("quantity") != null)
            doAmountChangeRequest(request, response);
        else if (request.getParameter("removeProductButton") != null)
            doRemoveProductButtonRequest(request, response);
        else if (request.getParameter("placeOrder") != null)
            doPlaceOrderRequest(request, response);
    }

    private void doAmountChangeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        cartService.editProductAmountInCart(login, request.getParameterMap(), fieldToError);
        List<CartProduct> cartProductList = cartService.getCartProductList(login);
        BigDecimal totalForCart = cartService.calculateTotalForCart(cartProductList);

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("totalPrice", totalForCart);
            request.setAttribute("cartProductList", cartProductList);
        }

        request.setAttribute("totalPrice", totalForCart);
        request.setAttribute("cartProductList", cartProductList);
        request.getRequestDispatcher("/WEB-INF/views/shop/cart.jsp").forward(request, response);
    }

    private void doRemoveProductButtonRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        String productIdParam = request.getParameter("productId");
        if (productIdParam != null && productIdParam.matches("\\d+")) {
            Long productId = Long.parseLong(productIdParam);
            cartService.removeProductFromCart(login, productId, fieldToError);
        } else {
            fieldToError.put("param", "Nie podano productId");
        }

        List<CartProduct> cartProductList = cartService.getCartProductList(login);
        BigDecimal totalForCart = cartService.calculateTotalForCart(cartProductList);

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("totalPrice", totalForCart);
            request.setAttribute("cartProductList", cartProductList);
        }

        request.setAttribute("totalPrice", totalForCart);
        request.setAttribute("cartProductList", cartProductList);
        request.getRequestDispatcher("/WEB-INF/views/shop/cart.jsp").forward(request, response);
    }

    private void doPlaceOrderRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        ordersService.submitOrder(login, fieldToError);
        List<CartProduct> cartProductList = cartService.getCartProductList(login);
        BigDecimal totalForCart = cartService.calculateTotalForCart(cartProductList);

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("totalPrice", totalForCart);
            request.setAttribute("cartProductList", cartProductList);
        }

        response.sendRedirect(request.getContextPath() + "/shop/orders");
    }
}
