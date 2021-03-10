package com.example.salle.controller;

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

import com.example.salle.application.AmazonS3Service;
import com.example.salle.application.ProductService;
import com.example.salle.domain.ChatRoom;
import com.example.salle.domain.Product;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	@Autowired
    ProductService productService;
	
	@Autowired
	AmazonS3Service amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	
	@RequestMapping(value = "/product/register", method = RequestMethod.GET)
	public String sellAttemptPost(Model model) {
		
		Product product = new Product();
		
		model.addAttribute("product", product);	
		return "product/register";
	}
    
	//상품등록완료 페이지
    @RequestMapping(value = "/product/done", method = RequestMethod.POST)
    public String sellHandle(@ModelAttribute("product") Product product, Errors errors,
    	HttpSession httpSession, Model model) throws Exception {
		productService.registerProduct(httpSession, product, product_file, errors);
		
		if (errors.hasErrors()) 
			return "product/register";

		ChatRoom chatRoom = new ChatRoom();
		model.addAttribute("chatRoom", chatRoom);
    	return "product/productInfo";
    }
    
    //상품등록 이미지파일 업로드
    Product product_file = new Product();
    @RequestMapping(value= "/productReg/ajax", method= RequestMethod.POST)
    public void productRegAjax(HttpServletRequest req) throws Exception {
    	productService.insertImg(req, product_file, bucket);
    }

	
	
	

	
    
    

}
