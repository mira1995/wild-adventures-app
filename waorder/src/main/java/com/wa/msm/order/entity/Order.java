package com.wa.msm.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "orders", schema = "waorder")
@Data @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {

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

    @JsonManagedReference
    @OneToMany(mappedBy = "order", targetEntity = OrderSession.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<OrderSession>  orderSessions = new ArrayList<>(0);

    public void addOrderSession (OrderSession orderSession){
        orderSessions.add(orderSession);
    }

    public void removeOrderSession(OrderSession orderSession){
        orderSessions.remove(orderSession);
    }
}
