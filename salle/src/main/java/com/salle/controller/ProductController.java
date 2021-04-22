package com.salle.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.salle.application.AmazonS3Service;
import com.salle.application.ProductService;
import com.salle.domain.ChatMessage;
import com.salle.domain.Login;
import com.salle.domain.Product;

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

	
	@RequestMapping(value = "/product/add", method = RequestMethod.GET)
	public String productRegister(Model model, HttpSession httpSession) {
		if (httpSession.getAttribute("login") == null) {
			return "/login";
		} else {
			Login loginInfo = (Login) httpSession.getAttribute("login");
		}
		Product product = new Product();
		
		model.addAttribute("product", product);	
		return "product/add";
	}
    
	//상품등록완료 페이지
    @RequestMapping(value = "/product/done", method = RequestMethod.POST)
    public String productRegisterDone(@ModelAttribute("product") Product product, Errors errors,
    	HttpSession httpSession, Model model) throws Exception {
		productService.addProduct(httpSession, product, product_file, errors);
		
		if (errors.hasErrors()) 
			return "product/add";

		ChatMessage chatRoom = new ChatMessage();
		model.addAttribute("chatRoom", chatRoom);
		String url = "redirect:/productInfo/"+product.getPr_id();
    	return url;
    }
    
    //상품등록 이미지파일 업로드
    Product product_file = new Product();
    @ResponseBody
    @RequestMapping(value= "/productReg/ajax", method= RequestMethod.POST)
    public String productRegAjax(HttpServletRequest req) throws Exception {
    	productService.insertImg(req, product_file, bucket, 0);
    	return "success";
    }

	
	
	

	
    
    

}
