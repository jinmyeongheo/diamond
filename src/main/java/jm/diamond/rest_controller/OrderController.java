package jm.diamond.rest_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

   @PostMapping("/count/order")
   public ApiResponse countOrder(){
      return ApiResponse.OK;
   }

}
