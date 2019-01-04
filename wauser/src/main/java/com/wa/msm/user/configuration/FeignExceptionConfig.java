package com.wa.msm.user.configuration;

import com.wa.msm.user.web.exception.UserAccountErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public UserAccountErrorDecoder mImageErrorDecoder(){
        return new UserAccountErrorDecoder();
    }
}
