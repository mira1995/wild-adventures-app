package com.wa.msm.user.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageBadRequestException extends RuntimeException {

    public ImageBadRequestException(String message) {
        super(message);
    }
}
