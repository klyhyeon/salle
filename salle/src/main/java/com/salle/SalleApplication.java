package com.salle;

import java.util.Locale;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class SalleApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(SalleApplication.class)
			.run(args);
	}
//
//	
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		builder.sources(SpringApplication.class)
//				.properties("spring.config.location= classpath:application.properties, classpath:db.properties");
//				return builder;
//	}
//	

	@Bean
	public MessageSource messageSource() {
		Locale.setDefault(Locale.KOREA);
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("label/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
