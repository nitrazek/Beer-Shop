package pl.wipb.beershop.controllers;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.services.AuthenticationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final Logger log = Logger.getLogger(LoginController.class.getName());

    @EJB
    private AuthenticationService authService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(authService.verifyAccount(session))
            response.sendRedirect(request.getContextPath() + "/shop/products");
        else
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        Account account = authService.handleRegister(request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty() || account == null) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("login", request.getParameter("login"));
            request.setAttribute("email", request.getParameter("email"));

            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("login", account.getLogin());

        response.sendRedirect(request.getContextPath() + "/shop/products");
    }
}
