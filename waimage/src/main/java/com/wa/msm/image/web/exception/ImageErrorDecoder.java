package com.wa.msm.image.web.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class ImageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String s, Response response) {
        if(response.status() == 404){
            if(s.contains("MSAdventureProxy")) throw new AdventureNotFoundException("L'aventure recherchée n'existe pas");
            else if(s.contains("MSCategoryProxy")) throw new CategoryNotFoundException("La catégorie recherchée n'existe pas");
        }
        return defaultErrorDecoder.decode(s, response);
    }

}
