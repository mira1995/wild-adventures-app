package com.wa.msm.order.repository;

import com.wa.msm.order.entity.OrderDemandSession;
import com.wa.msm.order.entity.OrderDemandSessionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDemandSessionRepository extends JpaRepository<OrderDemandSession, OrderDemandSessionKey> {

}
