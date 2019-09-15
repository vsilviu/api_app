package ro.apisec.ms.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import ro.apisec.ms.demo.config.SecuredController;

@SecuredController
public class DenyAllMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(DenyAllMapping.class);

    @GetMapping("/deniedByDefault1")
    public void mapping1() {
        LOGGER.info("Denying on mapping 1");
    }

    @GetMapping("/deniedByDefault2")
    public void mapping2() {
        LOGGER.info("Denying on mapping 2");
    }

    @GetMapping("/accessible")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String accessible() {
        return "/securedController";
    }
}
