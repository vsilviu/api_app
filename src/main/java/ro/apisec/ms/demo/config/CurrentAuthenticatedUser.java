package ro.apisec.ms.demo.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class CurrentAuthenticatedUser {

    private CurrentAuthenticatedUser() {
        // no initialization
    }

    public static User get() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsername() {
        if (isAuthenticated()){
            return get().getUsername();
        }
        return "anonymous";
    }

    public static boolean isAuthenticated() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        if (authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }

        return true;
    }
}
