package jm.diamond.dao.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jm.diamond.JpaUnitTest;
import jm.diamond.dao.entity.OrderInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


class OrderInfoRepositoryTest extends JpaUnitTest {

   @Autowired
   private TestEntityManager testEntityManager;

   @Autowired
   private OrderInfoRepository orderInfoRepository;

   @Test
   void test(){

      List<OrderInfo> orderInfos = orderInfoRepository.selectOrders();
      List<OrderInfo> all = orderInfoRepository.findAll();
      System.out.println("all = " + all);
      System.out.println("orders = " + orderInfos);


      // Given
      OrderInfo orderInfo = OrderInfo.builder()
          .paymentMethod("1")
          .status("1")
          .amount(BigDecimal.ONE)
          .orderDateTime(LocalDateTime.now())
          .build();
      OrderInfo save = orderInfoRepository.save(orderInfo);

      // When
      Optional<OrderInfo> byId = orderInfoRepository.findById(save.getId());



      // Then AssertJ vs. JUnit junit보다 풍부한 기능 제공
      assertThat(byId.get()).as("check save order").isEqualTo(save);


//      for (int i = 0; i < 100; i++) {
//      Order build = Order.builder().seq("210202"+i).payReqAmt(BigDecimal.ONE).build();
//      Order save = orderRepository.save(build);
//      save.plusPayReqAmt();
//      testEntityManager.flush();
//      }
//      testEntityManager.clear();
//
//      Optional<Order> byId =
//          orderRepository.findById("2102020");
//
//      if(byId.isPresent()){
//         System.out.println("byId = " + byId.get().getSeq());
//      }else{
//         System.out.println("byId = " + byId.isPresent());
//      }


   }

}