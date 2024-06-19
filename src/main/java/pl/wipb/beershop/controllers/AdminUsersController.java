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
    private AccountService accountService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;
        if (login == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Account> accountList = accountService.getAllAccounts();

        request.setAttribute("accountList", accountList);
        request.getRequestDispatcher("/WEB-INF/views/admin-panel/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("filterButton") != null)
            doFilterRequest(request, response);
        else if (request.getParameter("deleteAccountButton") != null)
            doDeleteAccountButton(request, response);
    }

    private void doFilterRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        List<Account> accountList = accountService.getFilteredAccountList(request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty() || accountList == null) {
            accountList = accountService.getAllAccounts();
            request.setAttribute("errors", fieldToError);
            request.setAttribute("accountList", accountList);
            request.getRequestDispatcher("/WEB-INF/views/admin-panel/users.jsp").forward(request, response);
            return;
        }

        request.setAttribute("accountList", accountList);
        request.getRequestDispatcher("/WEB-INF/views/admin-panel/users.jsp").forward(request, response);
    }

    private void doDeleteAccountButton(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();

        String accountIdParam = request.getParameter("accountId");
        if (accountIdParam != null && accountIdParam.matches("\\d+")) {
            Long accountId = Long.parseLong(accountIdParam);
            accountService.deleteAccount(accountId, fieldToError);
        } else {
            fieldToError.put("param", "Id konta jest nieprawidłowe.");
        }

        List<Account> accountList = accountService.getAllAccounts();

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);
            request.setAttribute("accountList", accountList);

            request.getRequestDispatcher("/WEB-INF/views/admin-panel/users.jsp").forward(request, response);
            return;
        }

        request.setAttribute("accountList", accountList);
        request.getRequestDispatcher("/WEB-INF/views/admin-panel/users.jsp").forward(request, response);
    }
}
