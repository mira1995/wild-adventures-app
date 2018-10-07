package com.wa.msm.image.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ImageValidationException extends RuntimeException {
    public ImageValidationException(String message){
        super(message);
    }
}
