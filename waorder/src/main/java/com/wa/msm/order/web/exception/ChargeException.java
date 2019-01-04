package com.wa.msm.order.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ChargeException extends RuntimeException{
    public ChargeException(String message){
        super(message);
    }
}
