package jm.diamond.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Page<OrderHistory> getOrderHistoryPage() {
        return null;
    }
}
