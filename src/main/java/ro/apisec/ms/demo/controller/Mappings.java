package ro.apisec.ms.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ro.apisec.ms.demo.config.CurrentAuthenticatedUser;
import ro.apisec.ms.demo.controller.profiles.Stuff;

@Controller
public class Mappings {

    @Autowired
    private Stuff stuff;

    @GetMapping("/")
    public String home1() {
        stuff.execute();
        return "/home";
    }

    @GetMapping("/home")
    public String home() {
        return "/home";
    }

    @GetMapping("/error")
    public String error() {
        return "/home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/login")
    public String login() {
        if (CurrentAuthenticatedUser.isAuthenticated()) {
            return "redirect:/";
        }
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    @GetMapping("/methodAuthorization")
    @PreAuthorize("hasAuthority('SOME_UNDEFINED_AUTHORITY')")
    public String authorizationOnMethod() {
        return "/error/403";
    }

    @GetMapping("/loginDisabled")
    public String loginDisabled() {
        return "/loginDisabled";
    }
}
