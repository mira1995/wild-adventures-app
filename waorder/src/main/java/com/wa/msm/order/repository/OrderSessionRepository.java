package com.wa.msm.order.repository;

import com.wa.msm.order.entity.OrderSession;
import com.wa.msm.order.entity.OrderSessionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSessionRepository extends JpaRepository<OrderSession, OrderSessionKey> {
}
