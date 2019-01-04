package com.wa.msm.order.web.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class OrderErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        if(response.status() == 404){
            if(s.contains("MSAdventureProxy")) throw new SessionNotFoundException("La session recherchée n'existe pas");
            else if(s.contains("MSUserAccountProxy")) throw new UserAccountNotFoundException("L'utilisateur recherché n'existe pas");
        }
        if(response.status() == 422){
            if(s.contains("MSUserAccountProxy")) throw new UserAccountNotFoundException("L'utilisateur n'est pas valide");
        }

        return defaultErrorDecoder.decode(s, response);

    }

}
