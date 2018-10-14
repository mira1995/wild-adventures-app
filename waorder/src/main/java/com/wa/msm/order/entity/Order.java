package com.wa.msm.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order", schema = "waorder")
@Data @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order extends AbstractOrder implements Serializable {

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
