package com.example.salle.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor	
public class AmazonS3Service {
	
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	public String getPresignedUrl() {
		
		String fileName = "static/img/searchicon.png";
		//String url = amazonS3.generatePresignedUrl(bucket, fileName, new Date()).toString();
		return amazonS3.getUrl(bucket, fileName).toString();
	}
	
	public void uploadImg(String bucket, String fileName, MultipartFile file) throws IOException {
		
		File uploadFile = convert(file);
		
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	public File convert(MultipartFile file) throws IOException {
		
		File uploadFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(uploadFile);
		fos.write(file.getBytes());
		return uploadFile;
	}

}
