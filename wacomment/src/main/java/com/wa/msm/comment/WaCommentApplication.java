package com.wa.msm.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.wa.msm.comment")
@EnableDiscoveryClient
public class WaCommentApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaCommentApplication.class, args);
	}
}
