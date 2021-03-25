package com.salle;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SalleApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(SalleApplication.class)
			.run(args);
	}
}
