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

        return "login.html";
    }

    @GetMapping("/sso")
    public RedirectView sso() {
        // Keycloak Authorization Endpoint
        String authorizationEndpoint = "http://localhost:8080/realms/nawabari/protocol/openid-connect/auth";

        // state / nonce 값은 반드시 무작위 생성 (CSRF / Replay 방어용)
        String state = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();

        // 안전하게 인코딩된 쿼리스트링 구성
        URI redirectUri = UriComponentsBuilder
                .fromHttpUrl(authorizationEndpoint)
                .queryParam("client_id", "diamond")
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", "http://localhost:8888/sso/callback") // 꼭 인코딩됨
                .queryParam("state", state)
                .queryParam("nonce", nonce)
                .build()
                .encode() // ✅ 자동으로 RFC3986 인코딩 수행
                .toUri();

        // 👇 스프링이 자동으로 Keycloak /authorize 엔드포인트로 리다이렉트함
        return new RedirectView(redirectUri.toString());
    }

    @GetMapping("/sso/callback")
    public ResponseEntity<Object> loginCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String session_state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String error_description
    ) {

        log.debug("code :: {}", code);
        log.debug("state :: {}", state);
        log.debug("session_state :: {}", session_state);
        log.debug("error :: {}", error);
        log.debug("error_description :: {}", error_description);

        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logout() {

        return "error.html";
    }

    @GetMapping("/home")
    public String home() {

        return "main.html";
    }

}
