package jm.diamond.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ViewController {

    @GetMapping("/error")
    public String errorPage(){
        log.info("wajdfladkfj");
        return "/error.html";
    }
}
