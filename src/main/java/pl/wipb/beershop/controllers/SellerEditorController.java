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
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;

@WebServlet("/seller/editor/*")
public class SellerEditorController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (request.getPathInfo() != null && request.getPathInfo().startsWith("/")) {
            try {
                String productIdParam = request.getPathInfo().substring(1);
                int productId = Integer.parseInt(productIdParam);

                //request.setAttribute("", "");
            } catch (Exception e) {
                log.warn("Error - wrong parameters in path: " + request.getPathInfo());
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/seller-panel/product-editor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
