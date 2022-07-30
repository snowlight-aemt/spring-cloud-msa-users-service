package me.snowlight.springcloudusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import feign.Logger;
import me.snowlight.springcloudusers.error.FeignErrorDecoder;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class SpringCloudUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudUsersApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public FeignErrorDecoder getfeignErrorDecoder() {
		return new FeignErrorDecoder();
	}
}
