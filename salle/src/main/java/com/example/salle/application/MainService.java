package com.example.salle.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.s3.AmazonS3;

public class MainService {
	
	@Autowired
	AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	String bucket;
	
	public String getPresignedUrl() {
		String fileName = "/static/img/" + "searchicon.png";
		String url = amazonS3.generatePresignedUrl(bucket, fileName, null).toString();
		return url;
	}

}
