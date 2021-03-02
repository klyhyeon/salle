package com.example.salle;

import java.util.Locale;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@SpringBootApplication
public class SalleApplication extends SpringBootServletInitializer {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:/aws.properties,"
			+ "classpath:/application.properties";
	
    public static void main(String[] args) {
    	SpringApplication.run(SalleApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder
        		.sources(SalleApplication.class)
        		.properties(APPLICATION_LOCATIONS);
    }
    
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		//Member 클래스 경로
		//sessionFactoryBean.setTypeAliasesPackage("com.example.salle.domain");
		return sessionFactoryBean.getObject();
	}

	
	@Bean
	public MessageSource messageSource() {
		Locale.setDefault(Locale.KOREA);
		ResourceBundleMessageSource messageSource =
				new ResourceBundleMessageSource();
		messageSource.setBasename("label/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Bean
	public AmazonS3 amazonS3client() {
		
		AmazonS3 s3Client = 
				AmazonS3ClientBuilder.standard()
				.withRegion("ap-northeast-2")
				.withCredentials(null)
				.build();	
		return s3Client;
	}

}
