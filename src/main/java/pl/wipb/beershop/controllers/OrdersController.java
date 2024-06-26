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
import pl.wipb.beershop.models.Order;

import pl.wipb.beershop.services.OrdersService;

import java.io.IOException;
import java.util.List;

@WebServlet("/shop/orders")
public class OrdersController extends HttpServlet {

    private static final Logger log = LogManager.getLogger();
    @EJB
    private OrdersService orderService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        List<Order> orderList = orderService.getOrders(login);

        if (orderList == null) {
            response.sendRedirect(request.getContextPath() + "login");
            return;
        }

        request.setAttribute("orderList", orderList);
        request.getRequestDispatcher("/WEB-INF/views/shop/orders.jsp").forward(request, response);
    }
}
