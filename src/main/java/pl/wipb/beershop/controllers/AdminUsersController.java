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
import java.util.List;
import java.util.Map;

@WebServlet("/admin/users")
public class AdminUsersController extends HttpServlet {
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

        List<Account> accountList = authService.getAllAccounts();

        request.setAttribute("accountList", accountList);
        request.getRequestDispatcher("/WEB-INF/views/admin-panel/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("filterButton") != null)
            doFilterRequest(request, response);
        else if (request.getParameter("addAccountButton") != null)
            doAddAccountButton(request, response);
        else if (request.getParameter("editAccountButton") != null)
            doEditAccountButton(request, response);
        else if (request.getParameter("deleteAccountButton") != null)
            doDeleteAccountButton(request, response);
    }

    private void doFilterRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        List<Account> accountList = authService.getFilteredAccountList(request.getParameterMap(), fieldToError);
    }

    private void doAddAccountButton(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
    }

    private void doEditAccountButton(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
    }

    private void doDeleteAccountButton(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
    }
}
