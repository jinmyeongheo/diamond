package jm.diamond.dao.repository;

import java.math.BigDecimal;
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

      for (int i = 0; i < 100; i++) {
      Order build = Order.builder().seq("210202"+i).payReqAmt(BigDecimal.ONE).build();
      Order save = orderRepository.save(build);
      save.plusPayReqAmt();

      List<Order> orders = orderRepository.selectOrders();
         System.out.println("orders = " + orders);
      testEntityManager.flush();
      }
      testEntityManager.clear();

      Optional<Order> byId =
          orderRepository.findById("2102020");

      if(byId.isPresent()){
         System.out.println("byId = " + byId.get().getSeq());
      }else{
         System.out.println("byId = " + byId.isPresent());
      }


   }

}