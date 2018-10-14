package com.wa.msm.order.configuration;

import com.wa.msm.order.web.exception.OrderErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public OrderErrorDecoder mOrderErrorDecoder(){return new OrderErrorDecoder();}
}
