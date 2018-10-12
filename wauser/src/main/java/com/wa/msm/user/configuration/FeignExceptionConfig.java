package com.wa.msm.user.configuration;

import com.wa.msm.user.web.exception.ImageErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public ImageErrorDecoder mImageErrorDecoder(){
        return new ImageErrorDecoder();
    }
}
