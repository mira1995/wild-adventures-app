package com.wa.msm.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "order", schema = "waorder")
@Data @NoArgsConstructor @AllArgsConstructor
public class Order implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private Calendar orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "useraccount_id")
    private Long userAccountId;

    @OneToMany(targetEntity = OrderSession.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<OrderSession>  orderSessions = new ArrayList<>(0);

}
