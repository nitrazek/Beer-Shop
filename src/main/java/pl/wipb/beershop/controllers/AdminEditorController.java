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
import pl.wipb.beershop.services.AuthenticationService;
import pl.wipb.beershop.services.ProductsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/editor/*")
public class AdminEditorController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private AuthenticationService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userId = request.getParameter("userId");
        if (userId != null && userId.matches("\\d+")) {
            Account account = authService.getAccount(Long.parseLong(userId));
            if(account != null) request.setAttribute("account", account);
        }

        request.getRequestDispatcher("/WEB-INF/views/admin-panel/user-editor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}