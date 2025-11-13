package jm.diamond.controller.sso;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Controller
public class SsoController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "main";
    }

    @GetMapping("/logout/success")
    public String logoutSuccess() {
        return "logout-success"; // Thymeleaf 페이지 등
    }

    @GetMapping("/temp")
    public String temp() {
        return "temp"; // Thymeleaf 페이지 등
    }
}
