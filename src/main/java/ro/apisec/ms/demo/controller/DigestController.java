package ro.apisec.ms.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigestController {
    @GetMapping("/home")
    public String digest() {
        return "open page";
    }

    @GetMapping(value = "/secure")
    public String secure() {
        return "secured page";
    }
}
