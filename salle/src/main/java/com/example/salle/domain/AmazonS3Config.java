package com.example.salle.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
	
	@Value("${cloud.aws.credentials.accessKey}")
	private String accesskey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	private String secretkey;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(accesskey, secretkey);
	}
	
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
				
	}

}
