package jm.diamond.service;

import org.springframework.data.domain.Page;

public interface OrderService {

   Page<OrderHistory> getOrderHistoryPage();

}
