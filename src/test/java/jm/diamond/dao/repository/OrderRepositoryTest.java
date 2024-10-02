package jm.diamond.dao.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jm.diamond.JpaUnitTest;
import jm.diamond.dao.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


class OrderRepositoryTest extends JpaUnitTest {

   @Autowired
   private TestEntityManager testEntityManager;

   @Autowired
   private OrderRepository orderRepository;

   @Test
   void test(){

      List<Order> orders = orderRepository.selectOrders();
      System.out.println("orders = " + orders);


      // Given
      Order order = Order.builder()
          .paymentMethod("1")
          .status("1")
          .totalAmount(BigDecimal.ONE)
          .orderDate(LocalDateTime.now())
          .build();
      Order save = orderRepository.save(order);

      // When
      Optional<Order> byId = orderRepository.findById(save.getId());



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