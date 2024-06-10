package pl.wipb.beershop.filters;

import jakarta.ejb.EJB;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pl.wipb.beershop.controllers.LoginController;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.services.AuthenticationService;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@WebFilter("/shop/*")
public class PermissionFilter extends HttpFilter {
    private final Logger log = Logger.getLogger(LoginController.class.getName());
    @EJB
    private AccountDao accountDao;
    @EJB
    private AuthenticationService authService;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info(()->
            "Request type:"+request.getMethod()
            +", contextPath:"+request.getContextPath()
            +", servletPath:"+request.getServletPath()
            +", pathInfo:"+request.getPathInfo()
        );

        if(authService.verifyAccount(request.getSession(false)))
            chain.doFilter(request, response);
        else
            response.sendRedirect(request.getContextPath() + "/login");
    }
}
