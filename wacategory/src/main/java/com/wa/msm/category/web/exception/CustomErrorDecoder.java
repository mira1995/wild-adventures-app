package com.wa.msm.category.web.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 404) {
            if (methodKey.contains("MSAdventureProxy")) return new AdventureNotFoundException("Aventure non trouvée");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
