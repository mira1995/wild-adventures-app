package com.wa.msm.image.configuration;

import com.wa.msm.image.web.exception.ImageErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public ImageErrorDecoder customErrorDecoder() {
        return new ImageErrorDecoder();
    }
}
