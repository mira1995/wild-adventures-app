package com.wa.msm.order.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderDemandNotFoundException extends RuntimeException {
    public OrderDemandNotFoundException(String message) {
        super(message);
    }
}
