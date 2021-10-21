package com.salle.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {
	@Bean
	public MessageSource messageSource() {
		Locale.setDefault(Locale.KOREA);
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("label/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
