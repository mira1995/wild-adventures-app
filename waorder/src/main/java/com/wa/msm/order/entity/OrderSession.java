package com.wa.msm.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_session", schema = "waorder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderSessionKey.class)
public class OrderSession implements Serializable {
    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Id
    @Column(name = "session_id")
    private Long sessionId;
}
