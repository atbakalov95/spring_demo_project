package com.example.reactivespringweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ReactivespringwebApplication {

	public static void main(String[] args) {
		ReactorDebugAgent.init();
		SpringApplication.run(ReactivespringwebApplication.class, args);
	}

}
