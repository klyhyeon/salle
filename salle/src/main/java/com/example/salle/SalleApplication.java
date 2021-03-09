package com.example.salle;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@SpringBootApplication
public class SalleApplication extends SpringBootServletInitializer {
	
    public static void main(String[] args) {
    	SpringApplication.run(SalleApplication.class,args);
    }

//    
//	@Bean
//	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//		sessionFactoryBean.setDataSource(dataSource);
//		return sessionFactoryBean.getObject();
//	}
	
	@Bean
	public MessageSource messageSource() {
		Locale.setDefault(Locale.KOREA);
		ResourceBundleMessageSource messageSource =
				new ResourceBundleMessageSource();
		messageSource.setBasename("label/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
//	@Bean
//	public AmazonS3 amazonS3() {
//		return AmazonS3ClientBuilder.standard().build();
//	}

	

}
