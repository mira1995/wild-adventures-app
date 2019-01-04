package com.wa.msm.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_demand_session", schema = "waorder")
@Data @NoArgsConstructor @AllArgsConstructor
@IdClass(OrderDemandSessionKey.class)
public class OrderDemandSession {

    @JsonBackReference
    @ManyToOne(targetEntity = OrderDemand.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OrderDemand orderDemand;

    @Column(name = "nb_order")
    private Long nbOrder;

    @Id
    @Column(name = "demand_id")
    private Long demandId;

    @Id
    @Column(name = "session_id")
    private Long sessionId;
}
