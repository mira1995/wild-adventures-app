package com.wa.msm.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.order")
@EnableEurekaClient
public class WaOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaOrderApplication.class, args);
	}
}
