package security.handlers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * If user tries to access a page they do not have appropriate permissions to,
 * it will log a warning, and redirect the user to the Access Denied page.
 *
 * Implements Springs AccessDeniedHandler interface
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOG
            = Logger.getLogger(String.valueOf(CustomAccessDeniedHandler.class));

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Creates Warning log if a user tries to access unauthorized page
            LOG.warning("User " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());
        }

        // Redirects to access-denied.html
        response.sendRedirect(request.getContextPath() + "/access-denied");
    }
}
