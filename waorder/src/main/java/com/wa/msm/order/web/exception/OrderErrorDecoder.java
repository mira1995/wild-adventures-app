package com.wa.msm.order.web.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class OrderErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        if(response.status() == 404)throw new UserAccountNotFoundException("L'utilisateur recherché n'existe pas");
        if(response.status() == 422)throw new UserAccountNotFoundException("L'utilisateur fourni n'est pas valide");

        return defaultErrorDecoder.decode(s, response);

    }


}
