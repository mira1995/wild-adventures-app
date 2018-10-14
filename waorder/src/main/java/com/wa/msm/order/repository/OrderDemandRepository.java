package com.wa.msm.order.repository;

import com.wa.msm.order.entity.OrderDemand;
import com.wa.msm.order.util.enumeration.OrderDemandEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDemandRepository extends JpaRepository<OrderDemand, Long> {

    List<OrderDemand> findByUserAccountId(Long userAccountId);

    List<OrderDemand> findByDemandStatus(OrderDemandEnum orderStatus);
}
