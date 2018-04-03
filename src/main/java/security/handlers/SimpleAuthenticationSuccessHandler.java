package security.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


/**
 *  When a user successfully logs in, SimpleAuthenticationSuccessHandler manages the
 *  redirect for which page they should go to, based on the authority granted to them
 *  by their role (user or admin).
 *
 *  Implements Spring's AuthenticationSuccess Handler interface
 */
@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public static final Logger LOG
            = Logger.getLogger(String.valueOf(SimpleAuthenticationSuccessHandler.class));

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // Lambda evaluates user login authority is User, Admin, or other (exception)
        authorities.forEach(authority -> {
            if(authority.getAuthority().equals("USER")) {
                try {
                    LOG.info("User " + authentication.getName()
                            + " is a " + authentication.getAuthorities() +
                            " and was redirected to /user/home");
                    // Redirects users to user homepage
                    redirectStrategy.sendRedirect(arg0, arg1, "/user/home");
                } catch (IOException e) {
                    // TODO add to Global Advice
                    e.printStackTrace();
                }
            } else if(authority.getAuthority().equals("ADMIN")) {
                try {
                    LOG.info("User " + authentication.getName()
                            + " is a " + authentication.getAuthorities() +
                            " and was redirected to /admin/home");
                    // Redirects admin to admin homepage
                    redirectStrategy.sendRedirect(arg0, arg1, "/admin/home");
                } catch (IOException e) {
                    // TODO add to Global Advice
                    e.printStackTrace();
                }
            } else {
                throw new IllegalStateException();
            }
        });

    }

}
