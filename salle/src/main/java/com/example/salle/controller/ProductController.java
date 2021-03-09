package com.example.salle.controller;

import java.io.File;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.salle.application.ProductService;
import com.example.salle.domain.ChatRoom;
import com.example.salle.domain.Login;
import com.example.salle.domain.Product;
import com.example.salle.domain.UuidImgname;
import com.example.salle.validation.SellProductValidation;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	@Autowired
    ProductService productService;
	
	@Autowired
	UuidImgname uuidImgname;
	
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	//상품등록 페이지
	@RequestMapping(value = "/sell/register", method = RequestMethod.GET)
    public String sellAttempt(Model model) {

		Product product = new Product();
		
    	model.addAttribute("product", product);	
        return "sell/register";
    }

	@RequestMapping(value = "/sell/register", method = RequestMethod.POST)
	public String sellAttemptPost(Model model) {
		
		Product product = new Product();
		
		model.addAttribute("product", product);	
		return "sell/register";
	}
    
	//상품등록완료 페이지
    @RequestMapping(value = "/sell/done", method = RequestMethod.POST)
    public String sellHandle(@ModelAttribute("product") Product product, Errors errors,
    		HttpSession httpSession, Model model) throws Exception {
    	
    	System.out.println("sell done");
    	
    	//파일 업로드 작업 수행(destination에 파일을 보내줌)

    	Login login = (Login) httpSession.getAttribute("login");
    	product.setPr_email(login.getEmail());
    	
    	product.setPr_title_alias(product.getPr_title().replaceAll("\\s", ""));
    	
		//ajax로 받은 img_file 정보를 넘겨줌 
		product.setPr_img_1(product_file.getPr_img_1());
		product.setPr_img_2(product_file.getPr_img_2());
		product.setPr_img_3(product_file.getPr_img_3());
		product.setPr_img_4(product_file.getPr_img_4());
		product.setPr_img_5(product_file.getPr_img_5());
		

    	
		new SellProductValidation().validate(product, errors);
		
		if (errors.hasErrors()) {
			return "sell/register";
		}
		

		productService.registerProduct(product);
		
		product_file.setPr_img_1(null);
		product_file.setPr_img_2(null);
		product_file.setPr_img_3(null);
		product_file.setPr_img_4(null);
		product_file.setPr_img_5(null);

		ChatRoom chatRoom = new ChatRoom();
		model.addAttribute("chatRoom", chatRoom);
		
    	return "product/productInfo";
    }
    
    //상품등록 이미지파일 업로드
    Product product_file = new Product();

    @RequestMapping(value= "/sell/ajax", method= RequestMethod.POST)
    public void ajax(HttpServletRequest req) throws Exception {
    	    	
    	System.out.println("fileUpload ajax");
    	//formdata를 받은 req를 multipartfile로 타입 변환해줌
    	MultipartHttpServletRequest multi = (MultipartHttpServletRequest) req;
    	Iterator<String> iterator = multi.getFileNames(); 	
    	MultipartFile multipartFile = null;
    	System.out.println("fileUpload ajax get file");
    	System.out.println("testing git pulled");
    	

    	int reps = 0;
    	
    	while(iterator.hasNext()) {
    		
    		multipartFile = multi.getFile(iterator.next());
    		
    		String fileOriname = multipartFile.getOriginalFilename();
    		String uniName = uuidImgname.makeFilename(fileOriname);
    		String dirName = "/static/img";
    		String fileName = dirName + "/" + uniName;
    		
    		System.out.println("uploadFileName" + fileName);
    		amazonS3.putObject(new PutObjectRequest(bucket, fileName, (File) multipartFile));
    		

    		System.out.println("fileUpload ajax pre switch");
    		switch(reps) {
    		case 0: 
    			product_file.setPr_img_1(fileName);
    			System.out.println("sell done 1: " + product_file.getPr_img_1());
    			break;
    		case 1: 
    			product_file.setPr_img_2(fileName);
    			System.out.println("sell done 2: " + product_file.getPr_img_2());
    			break;
    		case 2: 
    			product_file.setPr_img_3(fileName);
    			System.out.println("sell done 3: " + product_file.getPr_img_3());
    			break;
    		case 3: 
    			product_file.setPr_img_4(fileName);
    			break;
    		case 4: 
    			product_file.setPr_img_5(fileName);
    			break;
    		}
    		
    		reps++;
    	}
    }

	
	
	

	
    
    

}
