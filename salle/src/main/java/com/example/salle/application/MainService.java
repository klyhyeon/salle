package com.example.salle.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Service
public class MainService {
	
	@Autowired
	AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	String bucket;
	
	public String getPresignedUrl() {
		amazonS3 = 
				AmazonS3ClientBuilder.standard()
				.withRegion("ap-northeast-2")
				.build();	
		
		String fileName = "/static/img/" + "searchicon.png";
		String url = amazonS3.generatePresignedUrl(bucket, fileName, null).toString();
		return url;
	}

}
