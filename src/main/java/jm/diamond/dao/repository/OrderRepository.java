package jm.diamond.dao.repository;

import jm.diamond.dao.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfo, Long>, OrderRepositoryCustom {


}
