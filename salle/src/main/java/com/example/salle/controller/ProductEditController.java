package com.example.salle.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.salle.application.ProductService;
import com.example.salle.domain.Login;
import com.example.salle.domain.Member;
import com.example.salle.domain.Product;
import com.example.salle.domain.UuidImgname;
import com.example.salle.validation.SellProductValidation;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductEditController {
	
	private final AmazonS3Client amazonS3Client;
	
	@Autowired
    ProductService productService;
	
	@Autowired
	UuidImgname uuidImgname;
    
    //상품등록 이미지파일 업로드
    Product product_file = new Product();
    
    @Value("${cloud.aws.s3.bucket}")
    String bucket; 

	//profile에서 판매글 수정, 삭제하기
	@RequestMapping(value= "/product/{pr_id}/edit", method = RequestMethod.GET)
	public String profileEdit(Model model, @PathVariable int pr_id) {
				
		Product product = productService.getProductInfo(pr_id);
		//get ProductInfo and get img list
		List<String> imgList = new ArrayList<String>();
		for (int i = 1; i < 6; i++) {
			switch (i) {
			case 1:
				String img1 = product.getPr_img_1();				
				if(img1 != null)
					imgList.add(img1);
				break;
			case 2:
				String img2 = product.getPr_img_2();				
				if(img2 != null)
					imgList.add(img2);
				break;
			case 3:
				String img3 = product.getPr_img_3();				
				if(img3 != null)
					imgList.add(img3);
				break;
			case 4:
				String img4 = product.getPr_img_4();				
				if(img4 != null)
					imgList.add(img4);
				break;
			case 5:
				String img5 = product.getPr_img_5();				
				if(img5 != null)
					imgList.add(img5);
				break;

			default:
				break;
			}
		}
		
		model.addAttribute("product", product);
		model.addAttribute("imgList", imgList);
		imgList = null;
				
		return "product/productEdit"; 
	}
	

    @RequestMapping(value= "/sell/ajax/edit", method= RequestMethod.POST)
    public void ajaxEdit(@RequestBody String json) throws Exception {
    	
    	JSONObject jsn = new JSONObject(json);
    	
    	String[] pr_ex_img_arr = (String[]) jsn.get("pr_ex_img_arr");
    	int fileExlength = pr_ex_img_arr.length;
    	String pr_id_str = (String) jsn.get("pr_id");
    	int pr_id = Integer.parseInt(pr_id_str);
    	productTemp = productService.getProductInfo(pr_id);
    	String[] pr_img_arr = new String[5];
    	if (fileExlength > 0) {
    		pr_img_arr[0] = productTemp.getPr_img_1(); 
    		pr_img_arr[1] = productTemp.getPr_img_2();
    		pr_img_arr[2] = productTemp.getPr_img_3();
    		pr_img_arr[3] = productTemp.getPr_img_4();
    		pr_img_arr[4] = productTemp.getPr_img_5();
    		productTemp.setPr_img_1(null);
    		productTemp.setPr_img_2(null);
    		productTemp.setPr_img_3(null);
    		productTemp.setPr_img_4(null);
    		productTemp.setPr_img_5(null);
    		//delete할 파일만 배열에 남겨두기 
    		for (String string : pr_ex_img_arr) {
    			for (int j = 0; j < 5; j++) {
    				if (string.equals(pr_img_arr[j])) {
    					pr_img_arr[j] = null;
    				}	    		
    			}
    		}
    		//남아있는 파일 setter 할당하기
    		if (fileExlength >= 1) {
    			productTemp.setPr_img_1(pr_ex_img_arr[0]);	    		
    		}
    		if (fileExlength >= 2) {
    			productTemp.setPr_img_2(pr_ex_img_arr[1]);	    			    		
    		}
    		if (fileExlength >= 3) {
    			productTemp.setPr_img_3(pr_ex_img_arr[2]);	    			    		
    		}
    		if (fileExlength >= 4) {
    			productTemp.setPr_img_4(pr_ex_img_arr[3]);	    			    		
    		}
    		if (fileExlength >= 5) {
    			productTemp.setPr_img_5(pr_ex_img_arr[4]);	    			    		
    		}	
    	}	
    	for (int i = 0; i < 5; i++) {
    		if (pr_img_arr[i] == null || pr_img_arr[i] == "")
    			continue;
    		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, pr_img_arr[i]));
    	} //delete 파일
    	
    	MultipartHttpServletRequest multi = (MultipartHttpServletRequest) jsn.get("formData");
    	Iterator<String> iterator = multi.getFileNames();
    	MultipartFile multipartFile = null;  
    	int reps = fileExlength;
    
    	while(iterator.hasNext()) {
    		multipartFile = multi.getFile(iterator.next());   		
    		multipartFile = multi.getFile(iterator.next());
    		multipartFile.transferTo(new File(""));
    		String fileOriname = multipartFile.getOriginalFilename();
    		String uniName = uuidImgname.makeFilename(fileOriname);
    		String dirName = "/static/img";
    		String fileName = dirName + "/" + uniName;
    		
    		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, (File) multipartFile));
    		
    		switch(reps) {
    		case 0: 
    			productTemp.setPr_img_1(fileName);
    			break;
    		case 1: 
    			productTemp.setPr_img_2(fileName);
    			break;
    		case 2: 
    			productTemp.setPr_img_3(fileName);
    			break;
    		case 3: 
    			productTemp.setPr_img_4(fileName);
    			break;
    		case 4: 
    			productTemp.setPr_img_5(fileName);
    			break;
    		}
    		reps++;
    	}//새로운 파일 setter 할당
    }
	
	Product productTemp;
	//edit save
	@RequestMapping(value= "/product/{pr_id}/save", method= RequestMethod.POST)
	public String profileEditDone(@ModelAttribute("product") Product product, Errors errors,
			HttpSession httpSession, @PathVariable("pr_id") int pr_id) {
		
		System.out.println("saving process");
		
    	Login login = (Login) httpSession.getAttribute("login");
    	product.setPr_email(login.getEmail());
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
		
		if (errors.hasErrors()) {
			return "product/productEdit";
		}
		
		productService.updateProduct(product);
		
		return "product/productInfo";
	} 
    	
	String flag;	
	@RequestMapping(value= "/product/{pr_id}/delete", method= RequestMethod.GET)
	public String profileDelete(Model model, @PathVariable int pr_id) throws UnsupportedEncodingException {
		
		Product product = productService.getProductInfo(pr_id);
		
		//for pr_email to profile controller
		model.addAttribute("product", product);
		//delete only if confirm is yes
		if (flag.equals("true")) {
			productService.deleteProduct(pr_id);
		}
		
		String nickName = productService.getMemberProductInfo(product.getPr_email());
		
		String nickNameEncode = URLEncoder.encode(nickName, "UTF-8");
		
		return "redirect:/profile/" + nickNameEncode;
	}
	
	@RequestMapping(value= "/ajax/delete", method=RequestMethod.POST)
	public void ajaxDelete(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		
		flag = (String) jsn.get("flag");
	
		System.out.println("flag: " + flag);
	
	}
	
    
    

}
