package com.example.salle.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor	
public class MainService {
	
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	public String getPresignedUrl() {
		
		String fileName = "static/img/searchicon.png";
		//String url = amazonS3.generatePresignedUrl(bucket, fileName, new Date()).toString();
		return amazonS3.getUrl(bucket, fileName).toString();
	}

}
