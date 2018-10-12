package com.wa.msm.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSessionKey implements Serializable {
    private Long orderId;
    private Long sessionId;
}
