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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final Logger log = LogManager.getLogger();
    @EJB
    private AuthenticationService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        if(authService.verifyAccount(login)) {
            if (login != null) session.removeAttribute("login");
            response.sendRedirect(request.getContextPath() + "/shop/products");
        } else
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        Account account = authService.handleLogin(request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty() || account == null) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("login", request.getParameter("login"));

            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("login", account.getLogin());

        response.sendRedirect(request.getContextPath() + "/shop/products");
    }
}
