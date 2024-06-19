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

@WebServlet("/admin/editor/*")
public class AdminEditorController extends HttpServlet {
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

        String accountId = request.getParameter("accountId");
        if (accountId != null && accountId.matches("\\d+")) {
            Account account = accountService.getAccount(Long.parseLong(accountId));
            if(account != null) request.setAttribute("account", account);
        }

        request.getRequestDispatcher("/WEB-INF/views/admin-panel/user-editor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> fieldToError = new HashMap<>();
        accountService.addOrEditAccount(request.getParameterMap(), fieldToError);

        if(!fieldToError.isEmpty()) {
            request.setAttribute("errors", fieldToError);

            String accountId = request.getParameter("accountId");
            if (accountId != null && accountId.matches("\\d+")) {
                Account account = accountService.getAccount(Long.parseLong(accountId));
                if(account != null) request.setAttribute("account", account);
            }

            request.getRequestDispatcher("/WEB-INF/views/admin-panel/user-editor.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}