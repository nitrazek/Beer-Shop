package pl.wipb.beershop.filters;

import jakarta.ejb.EJB;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.services.AuthenticationService;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter extends HttpFilter {
    private static final Logger log = LogManager.getLogger();
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

        HttpSession session = request.getSession(false);
        String login = (session != null) ? (String) session.getAttribute("login") : null;

        if(authService.verifyAdmin(login))
            chain.doFilter(request, response);
        else {
            if (login != null) session.removeAttribute("login");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
