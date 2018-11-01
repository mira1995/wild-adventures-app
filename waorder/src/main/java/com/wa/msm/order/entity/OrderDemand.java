package com.wa.msm.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wa.msm.order.util.enumeration.OrderDemandEnum;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "order_demand", schema = "waorder")
@Data @NoArgsConstructor @AllArgsConstructor
public class OrderDemand implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private Calendar orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatusEnum status;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "useraccount_id")
    private Long userAccountId;

    @OneToOne(targetEntity = Order.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "demand_type")
    private OrderDemandEnum demandStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "orderDemand", targetEntity = OrderDemandSession.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<OrderDemandSession> orderDemandSessions = new ArrayList<>(0);

    @Column(name = "demand_message")
    private String demandMessage;

    @Column(name = "answer_message")
    private String answerMessage;

    public void addOrderDemandSessions(OrderDemandSession orderDemandSession){
        orderDemandSessions.add(orderDemandSession);
    }

    public void removeOrderDemandSessions(OrderDemandSession orderDemandSession){
        orderDemandSessions.remove(orderDemandSession);
    }
}
