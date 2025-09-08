package jm.diamond.dao.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import jm.diamond.dao.entity.OrderInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static jm.diamond.dao.entity.QOrderInfo.orderInfo;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
   private final JPAQueryFactory jpaQueryFactory;

   @Override
   public List<OrderInfo> selectOrders() {
      return jpaQueryFactory.selectFrom(orderInfo).fetch();
   }
}
