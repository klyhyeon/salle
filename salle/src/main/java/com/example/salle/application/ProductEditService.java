package com.example.salle.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.salle.domain.Login;
import com.example.salle.domain.Product;
import com.example.salle.domain.UuidImgname;
import com.example.salle.validation.SellProductValidation;

@Service
public class ProductEditService {
	
	private final Logger log = LoggerFactory.getLogger(ProductEditService.class);
	
	@Autowired
	ProductService productService;
	
	@Autowired
	AmazonS3Service amazonS3;
	
	@Autowired
	UuidImgname uuidImg;
	
	public Product productEdit(int pr_id, List<String> imgList) {
		
		Product product = productService.getProductInfo(pr_id);
		//get ProductInfo and get img list
		String img1 = product.getPr_img_1();				
		String img2 = product.getPr_img_2();				
		String img3 = product.getPr_img_3();				
		String img4 = product.getPr_img_4();				
		String img5 = product.getPr_img_5();				

		for (int i = 1; i < 6; i++) {
			switch (i) {
			case 1:
				if(img1 != null)
					imgList.add(img1);
				break;
			case 2:
				if(img2 != null)
					imgList.add(img2);
				break;
			case 3:
				if(img3 != null)
					imgList.add(img3);
				break;
			case 4:
				if(img4 != null)
					imgList.add(img4);
				break;
			case 5:
				if(img5 != null)
					imgList.add(img5);
				break;
			}
		}
		return product;
	}

	
	public void imgEdit(HttpServletRequest http, Product productUpdate, String bucket) throws JSONException, IOException {
		String[] exImgArr = (String[]) http.getAttribute("exImgArr");
    	int length = exImgArr.length;
    	int exImgArrLength = exImgArr.length;
    	log.info("exImgArr[0]" + exImgArr[0]);
    	String pr_id_str = (String) http.getAttribute("pr_id");
    	int pr_id = Integer.parseInt(pr_id_str);
    	productUpdate = productService.getProductInfo(pr_id);
    	String[] prImgArr = new String[5];
    	
    	if (exImgArrLength > 0) {
    		prImgArr[0] = productUpdate.getPr_img_1(); 
    		prImgArr[1] = productUpdate.getPr_img_2();
    		prImgArr[2] = productUpdate.getPr_img_3();
    		prImgArr[3] = productUpdate.getPr_img_4();
    		prImgArr[4] = productUpdate.getPr_img_5();
    		productUpdate.setPr_img_1(null);
    		productUpdate.setPr_img_2(null);
    		productUpdate.setPr_img_3(null);
    		productUpdate.setPr_img_4(null);
    		productUpdate.setPr_img_5(null);
    		//delete할 파일만 배열에 남겨두기 
    		for (String exImg : exImgArr) {
    			for (int j = 0; j < 5; j++) {
    				if (exImg.equals(prImgArr[j]))
    					prImgArr[j] = null;
    			}
    		}
    		//남아있는 파일 setter 할당하기
    		if (exImgArrLength >= 1) 
    			productUpdate.setPr_img_1(exImgArr[0]);	    		
    		if (exImgArrLength >= 2) 
    			productUpdate.setPr_img_2(exImgArr[1]);	    			    		
    		if (exImgArrLength >= 3)
    			productUpdate.setPr_img_3(exImgArr[2]);	    			    		
    		if (exImgArrLength >= 4)
    			productUpdate.setPr_img_4(exImgArr[3]);	    			    		
    		if (exImgArrLength >= 5)
    			productUpdate.setPr_img_5(exImgArr[4]);
    	}
    	
    	for (int i = 0; i < 5; i++) {
    		//TODO: for문 안에서 return과 continue 차이 
    		if (prImgArr[i] == null || prImgArr[i] == "")
    			continue;
    		switch (i) {
			case 0:
				productService.deleteImg1(pr_id);
				break;
			case 1:
				productService.deleteImg2(pr_id);
				break;
			case 2:
				productService.deleteImg3(pr_id);
				break;
			case 3:
				productService.deleteImg4(pr_id);
				break;
			case 4:
				productService.deleteImg5(pr_id);
				break;
			default:
				break;
			}
    		amazonS3.deleteFile(bucket, prImgArr[i]);
    	} //delete 파일
    	log.info("Prepassing insertImgEdit");
    	insertImgEdit(http, productUpdate, bucket, exImgArrLength);
    	log.info("Passing insertImgEdit");
	}
	

	private void insertImgEdit(HttpServletRequest http, Product productUpdate, String bucket,
			int exImgArrLength) throws IOException {
		log.info("insertImgEdit in processing");
    	MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) http;
    	Iterator<String> iterator = multiReq.getFileNames(); 	
    	MultipartFile multipartFile = null;
    	int reps = exImgArrLength;
    	int idx = 0;
    	while (iterator.hasNext()) {
    		multipartFile = multiReq.getFile(iterator.next());
    		String fileOriname = multipartFile.getOriginalFilename();
    		String ranCode = uuidImg.makeFilename(fileOriname);
    		String dirName = "static/img";
    		String fileName = dirName + "/" + ranCode;
    		
    		if (reps == 0) {
    			productUpdate.setPr_img_1(fileName);    			
    		} else if (reps == 1) {
    			productUpdate.setPr_img_2(fileName);    			
    		} else if (reps == 2) {
    			productUpdate.setPr_img_3(fileName);    			
    		} else if (reps == 3) {
    			productUpdate.setPr_img_4(fileName);    			
    		} else if (reps == 4) {
    			productUpdate.setPr_img_5(fileName);    			
    		}
    		reps++;
    		amazonS3.uploadImg(bucket, fileName, multipartFile);
	}
}

	public void productSave(Product product, Product productUpdate,
			HttpSession httpSession, Errors errors) {
		
	   	Login loginInfo = (Login) httpSession.getAttribute("login");
    	product.setPr_email(loginInfo.getEmail());
    	product.setPr_title_alias(product.getPr_title().replaceAll("\\s", ""));
    	
    	String[] imgArr = new String[5];
    	imgArr[0] = productUpdate.getPr_img_1();
    	imgArr[1] = productUpdate.getPr_img_2();
    	imgArr[2] = productUpdate.getPr_img_3();
    	imgArr[3] = productUpdate.getPr_img_4();
    	imgArr[4] = productUpdate.getPr_img_5();
    	
    	product.setPr_img_1(imgArr[0]);
    	product.setPr_img_2(imgArr[1]);
    	product.setPr_img_3(imgArr[2]);
    	product.setPr_img_4(imgArr[3]);
    	product.setPr_img_5(imgArr[4]);
   
		//ajax로 받은 img_file 정보를 넘겨줌 
    	
		new SellProductValidation().validate(product, errors);
	}

	public String productDelete(int pr_id) throws UnsupportedEncodingException {
		Product product = productService.getProductInfo(pr_id);
		String nickName = productService.getMemberProductInfo(product.getPr_email());	
		String nickNameEncode = URLEncoder.encode(nickName, "UTF-8");
		return nickNameEncode;
	}
	
	

}
