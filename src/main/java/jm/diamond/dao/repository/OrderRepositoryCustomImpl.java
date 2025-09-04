package jm.diamond.dao.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import jm.diamond.dao.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static jm.diamond.dao.entity.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
   private final JPAQueryFactory jpaQueryFactory;

   @Override
   public List<Order> selectOrders() {
      return jpaQueryFactory.selectFrom(order).fetch();
   }
}
