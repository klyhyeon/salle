package com.example.salle.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

@Service
public class MainService {
	
	@Autowired
	AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	public String getPresignedUrl() {
		
		String fileName = "searchicon.png";
		//String url = amazonS3.generatePresignedUrl(bucket, fileName, new Date()).toString();
		return amazonS3.getObject(bucket, fileName).getObjectContent().toString();
	}

}
