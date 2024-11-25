package jm.diamond.rest_controller;

import jm.diamond.controller.OrderReq;
import jm.diamond.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

   private final OrderService orderService;

   @PostMapping("/count/order")
   public ApiResponse countOrder(@RequestBody OrderReq orderReq){


      return ApiResponse.OK;
   }

}
