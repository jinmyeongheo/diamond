package jm.diamond.dao.repository;

import java.util.List;
import jm.diamond.dao.entity.Order;

public interface OrderRepositoryCustom {
   List<Order> selectOrders();
}
