package com.wa.msm.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_session", schema = "waorder")
@Data @NoArgsConstructor @AllArgsConstructor
@IdClass(OrderSessionKey.class)
public class OrderSession implements Serializable {

    @JsonBackReference
    @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    @Column(name = "nb_order")
    private Long nbOrder;

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Id
    @Column(name = "session_id")
    private Long sessionId;
}
