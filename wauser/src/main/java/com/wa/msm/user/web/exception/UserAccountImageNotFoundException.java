package com.wa.msm.user.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserAccountImageNotFoundException extends RuntimeException {
    public UserAccountImageNotFoundException(String message){super(message);}
}
