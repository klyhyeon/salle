package com.example.salle.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.example.salle.domain.Login;
import com.example.salle.domain.Product;
import com.example.salle.validation.SellProductValidation;

@Service
public class ProductEditService {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	AmazonS3Service amazonS3;
	
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

	public void imgEdit(String json, Product productTemp, String bucket) throws JSONException, IOException {
	   	
    	JSONObject jsn = new JSONObject(json);
    	String[] exImgArr = (String[]) jsn.get("exImgArr");
    	int exImgArrlength = exImgArr.length;
    	String pr_id_str = (String) jsn.get("pr_id");
    	int pr_id = Integer.parseInt(pr_id_str);
    	productTemp = productService.getProductInfo(pr_id);
    	String[] prImgArr = new String[5];
    	
    	if (exImgArrlength > 0) {
    		prImgArr[0] = productTemp.getPr_img_1(); 
    		prImgArr[1] = productTemp.getPr_img_2();
    		prImgArr[2] = productTemp.getPr_img_3();
    		prImgArr[3] = productTemp.getPr_img_4();
    		prImgArr[4] = productTemp.getPr_img_5();
    		productTemp.setPr_img_1(null);
    		productTemp.setPr_img_2(null);
    		productTemp.setPr_img_3(null);
    		productTemp.setPr_img_4(null);
    		productTemp.setPr_img_5(null);
    		//delete할 파일만 배열에 남겨두기 
    		for (String exImg : exImgArr) {
    			for (int j = 0; j < 5; j++) {
    				if (exImg.equals(prImgArr[j]))
    					prImgArr[j] = null;
    			}
    		}
    		//남아있는 파일 setter 할당하기
    		if (exImgArrlength >= 1) 
    			productTemp.setPr_img_1(exImgArr[0]);	    		
    		if (exImgArrlength >= 2) 
    			productTemp.setPr_img_2(exImgArr[1]);	    			    		
    		if (exImgArrlength >= 3)
    			productTemp.setPr_img_3(exImgArr[2]);	    			    		
    		if (exImgArrlength >= 4)
    			productTemp.setPr_img_4(exImgArr[3]);	    			    		
    		if (exImgArrlength >= 5)
    			productTemp.setPr_img_5(exImgArr[4]);
    	}
    	
    	for (int i = 0; i < 5; i++) {
    		//TODO: for문 안에서 return과 continue 차이 
    		if (prImgArr[i] == null || prImgArr[i] == "")
    			continue;
    		amazonS3.deleteFile(bucket, prImgArr[i]);
    	} //delete 파일
		
    	productService.insertImg((HttpServletRequest)jsn.get("formData"), productTemp, bucket);
	}

	public void productSave(Product product, Product productTemp,
			HttpSession httpSession, Errors errors) {
		
	   	Login loginInfo = (Login) httpSession.getAttribute("login");
    	product.setPr_email(loginInfo.getEmail());
    	product.setPr_title_alias(product.getPr_title().replaceAll("\\s", ""));
    	
    	String[] imgArr = new String[5];
    	imgArr[0] = productTemp.getPr_img_1();
    	imgArr[1] = productTemp.getPr_img_2();
    	imgArr[2] = productTemp.getPr_img_3();
    	imgArr[3] = productTemp.getPr_img_4();
    	imgArr[4] = productTemp.getPr_img_5();
    	
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
