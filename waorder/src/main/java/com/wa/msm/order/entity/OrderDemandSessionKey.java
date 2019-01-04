package com.wa.msm.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDemandSessionKey implements Serializable {
    private Long demandId;
    private Long sessionId;
}
