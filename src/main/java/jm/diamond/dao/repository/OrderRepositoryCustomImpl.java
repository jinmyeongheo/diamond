package jm.diamond.dao.repository;

import static jm.diamond.dao.entity.QOrder.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import jm.diamond.dao.entity.Order;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
   private final JPAQueryFactory jpaQueryFactory;

   @Override
   public List<Order> selectOrders() {
      return jpaQueryFactory.selectFrom(order).fetch();
   }
}
