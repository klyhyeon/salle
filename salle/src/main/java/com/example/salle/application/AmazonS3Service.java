package com.example.salle.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor	
public class AmazonS3Service {
	
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	
	public String searchIcon() {
		String fileName = "static/img/searchicon.png";
		return amazonS3.getUrl(bucket, fileName).toString();
	}
	
	
	public String uploadImg(String bucket, String fileName, MultipartFile multiFile) throws IOException {
		File uploadFile = convert(multiFile);
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return "uploadS3 success";
	}

	public String uploadImgDirect(String bucket, String fileName, File file) throws IOException {
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		return "uploadS3 success";
	}
	
	
	public File convert(MultipartFile multiFile) throws IOException {
		File uploadFile = new File(multiFile.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(uploadFile);
		fos.write(multiFile.getBytes());
		fos.close();
		return uploadFile;
	}


	public void deleteFile(String bucket, String deleteFile) {
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, deleteFile));
	}

}
