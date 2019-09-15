package ro.apisec.ms.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import ro.apisec.ms.demo.config.SecuredController;

@SecuredController
public class BasicAuthController {

    @GetMapping("/basicAuthEndpoint")
    @PreAuthorize("hasAuthority('BASIC_AUTH')")
    public String basicAuth() {
        return "basicAuth";
    }
}
