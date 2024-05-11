package jm.diamond.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

  @GetMapping("/hello")
  public String sessionTest(){
    return "/index.html";
  }

}
