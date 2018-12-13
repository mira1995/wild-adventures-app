package com.wa.msm.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.comment")
@EnableEurekaClient
@EnableSwagger2
public class WaCommentApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaCommentApplication.class, args);
	}
}
