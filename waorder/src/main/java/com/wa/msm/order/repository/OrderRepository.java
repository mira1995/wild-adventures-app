package com.wa.msm.order.repository;

import com.wa.msm.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserAccountId(Long userAccountId);

}
