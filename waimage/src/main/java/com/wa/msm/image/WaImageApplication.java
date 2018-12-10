package com.wa.msm.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.image")
@EnableEurekaClient
@EnableSwagger2
public class WaImageApplication {
	public static void main(String[] args) {
		SpringApplication.run(WaImageApplication.class, args);
	}
}
