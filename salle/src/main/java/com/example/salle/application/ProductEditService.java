package com.example.salle.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

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

	
	public Product imgEdit(HttpServletRequest req, Product productUpdate, String bucket) throws JSONException, IOException {
		log.info("insertEdit in processing");
		Optional<String[]> exImgArrOpt = Optional.ofNullable(req.getParameterValues("imgExArr")); 
		String[] exImgArr = exImgArrOpt.orElse(null);
		int length = 0;
		int pr_id = Integer.parseInt(req.getParameter("pr_id"));
    	productUpdate = productService.getProductInfo(pr_id);
    	String[] prImgArr = new String[5];
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
		if (exImgArr != null) {
			length = exImgArr.length; 
			log.info("exImgArr length: " + length);
			for (String exImg : exImgArr) {
				for (int j = 0; j < 5; j++) {
					if (exImg.equals(prImgArr[j]))
						prImgArr[j] = null;
				}
			}
    		//남아있는 파일 setter 할당하기
    		if (length >= 1) 
    			productUpdate.setPr_img_1(exImgArr[0].substring(53));	    		
    		if (length >= 2) 
    			productUpdate.setPr_img_2(exImgArr[1].substring(53));	    			    		
    		if (length >= 3)
    			productUpdate.setPr_img_3(exImgArr[2].substring(53));	    			    		
    		if (length >= 4)
    			productUpdate.setPr_img_4(exImgArr[3].substring(53));	    			    		
    		if (length >= 5)
    			productUpdate.setPr_img_5(exImgArr[4].substring(53));
		}
    	
		for (int i = 0; i < 5; i++) {
    		if (prImgArr[i] != null || prImgArr[i] != "") {
    			log.info("deleteImg: " + prImgArr[i]);
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
				}
	    		amazonS3.deleteFile(bucket, prImgArr[i]);
    		}
    	} //delete 파일

    	productService.insertImg(req, productUpdate, bucket, length);
    	return productUpdate;
	}

	public Product productSave(Product product, Product productUpdate,
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
		return product;
	}

	public String productDelete(int pr_id) throws UnsupportedEncodingException {
		Product product = productService.getProductInfo(pr_id);
		String nickName = productService.getMemberProductInfo(product.getPr_email());	
		String nickNameEncode = URLEncoder.encode(nickName, "UTF-8");
		return nickNameEncode;
	}
	
	

}
