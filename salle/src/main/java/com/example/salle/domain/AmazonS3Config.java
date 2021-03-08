package com.example.salle.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
	
	@Value("${cloud.aws.credentials.accesskey}")
	private String accesskey;
	
	@Value("${cloud.aws.credentials.secretkey}")
	private String secretkey;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
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
