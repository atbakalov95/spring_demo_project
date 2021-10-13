package com.example.openidauthorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@ConfigurationPropertiesScan
public class OpenidauthorizationserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenidauthorizationserverApplication.class, args);
	}

}
