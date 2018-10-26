package com.wa.msm.user.web.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class UserAccountErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {

        if(response.status() == 404) return new ImageBadRequestException("L'image n'a pas été trouvée");
        if(response.status() == 422) return new ImageBadRequestException("L'image fournie n'est pas valide");
        if(response.status()>= 500  && response.status()<=599) return new ImageBadRequestException("Erreur lors de la connexion avec le microservice Image");

        return defaultErrorDecoder.decode(s, response);
    }
}
