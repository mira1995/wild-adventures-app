package com.wa.msm.category.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class AdventureNotFoundException extends RuntimeException {
    AdventureNotFoundException(String message) {
        super(message);
    }
}
