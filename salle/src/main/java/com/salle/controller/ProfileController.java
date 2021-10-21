package com.salle.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salle.application.ProductService;
import com.salle.application.ProfileService;
import com.salle.domain.Product;

@Controller
public class ProfileController {
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value = "/profile/{nickName}", method = RequestMethod.GET)
	public String profileReadGet(HttpSession session, @PathVariable String nickName, @RequestParam(value="pr_email", required = false) String pr_email,
			Model model) {
		
		model.addAttribute("nickName", nickName);
		
		List<Product> sellerProductList;
		
		if (pr_email == "") {
			sellerProductList = profileService.getSellerProductList(pr_email);
			model.addAttribute("totalProduct", profileService.getTotalProduct(pr_email));
		} else {
			Login login = (Login)session.getAttribute("login");
			sellerProductList = profileService.getSellerProductList(login.getEmail());
			model.addAttribute("totalProduct", profileService.getTotalProduct(login.getEmail()));			
		}
		//hoursfromupload
		Timestamp tsClient = Timestamp.valueOf(LocalDateTime.now());
		for (Product product : sellerProductList) {
			long diffTime = tsClient.getTime() - product.getPr_reg_date().getTime();
			int hours = (int) (diffTime / (1000 * 3600));
			if (hours < 1) {
				hours = 0;
			}
			product.setHoursFromUpload(hours);
		}
		
		model.addAttribute("sellerProductList", sellerProductList);
		
		return "profile";
	}
		

}
