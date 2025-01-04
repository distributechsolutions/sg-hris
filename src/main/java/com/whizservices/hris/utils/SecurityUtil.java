package com.whizservices.hris.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * A security utility class that provides the following functions:
 * 1.  Get the authenticated user
 * 2.  Calls the logout functionality of the application
 *
 * @author Gerald Paguio
 */
public class SecurityUtil {
    private static final String LOGOUT_SUCCESS_URL = "/whizhris";
    private static SecurityUtil INSTANCE;

    private SecurityUtil() {
    }

    public synchronized static SecurityUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SecurityUtil();
        }

        return INSTANCE;
    }

    public static UserDetails getAuthenticatedUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) securityContext.getAuthentication().getPrincipal();
        }

        // Anonymous or no authentication.
        return null;
    }

    public static void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
}
