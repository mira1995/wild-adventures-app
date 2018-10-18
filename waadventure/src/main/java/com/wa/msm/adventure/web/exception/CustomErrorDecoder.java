package com.wa.msm.adventure.web.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 404) {
            if (methodKey.contains("MSCategoryProxy")) return new CategoryNotFoundException("Catégorie non trouvée");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
