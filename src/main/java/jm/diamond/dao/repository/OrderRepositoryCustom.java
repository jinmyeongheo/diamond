package jm.diamond.dao.repository;

import java.util.List;
import jm.diamond.dao.entity.OrderInfo;

public interface OrderRepositoryCustom {
   List<OrderInfo> selectOrders();
}
