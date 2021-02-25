package com.example.salle.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.salle.application.ProductService;
import com.example.salle.domain.ChatRoom;
import com.example.salle.domain.Product;

@Controller
public class ProductInfoController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	Product product;
	
	ChatRoom chatRoom;
	
	//TODO: productInfo 뒤 seq 지정해야한다.
	@RequestMapping(value = "/productInfo/{pr_id}", method = RequestMethod.GET)
	public String productInfoGet(Model model, @PathVariable int pr_id) {		
		//chatRoom ModelAttribute
		ChatRoom chatRoom = new ChatRoom();
		model.addAttribute("chatRoom", chatRoom);
		
		
		//.jsp에서 ${product.pr_title}
		Product productInfo = productService.getProductInfo(pr_id);
		model.addAttribute("product", productInfo);
		
		List<String> productImgList = new ArrayList<String>();
		String img1 = productInfo.getPr_img_1();
		String img2 = productInfo.getPr_img_2();
		String img3 = productInfo.getPr_img_3();
		String img4 = productInfo.getPr_img_4();
		String img5 = productInfo.getPr_img_5();
		
		productImgList.add(img1);
		
		if (img2 != null) {
			productImgList.add(img2);
		}
		
		if (img3 != null) {
			productImgList.add(img3);
		}
		
		if (img4 != null) {
			productImgList.add(img4);
		}
		
		if (img5 != null) {
			productImgList.add(img5);
		}
		
		model.addAttribute("productInfoImg", productImgList);
		
		
		
		
		//member nickname
		model.addAttribute("nickName",productService.getMemberProductInfo(productInfo.getPr_email()));
		
		//hoursfromupload
	        Timestamp tsClient = Timestamp.valueOf(LocalDateTime.now());
	        long diffTime = tsClient.getTime() - productInfo.getPr_reg_date().getTime();
	        int hours = (int) (diffTime / (1000 * 3600));
	        if (hours < 1) {
	        	hours = 0;
	        }
	        productInfo.setHoursFromUpload(hours);
			model.addAttribute("hoursFromUpload",productInfo.getHoursFromUpload());
		
		return "product/productInfo";
	}
	
	@RequestMapping(value = "/productInfo/list", method = RequestMethod.GET)
	public String productListGet(Model model) {
		
		model.addAttribute("productListInfo", productService.getProductList());
		
		return "product/productList";
	}

	@RequestMapping(value = "/category/{pr_category}", method = RequestMethod.GET)
	public String productListGet(Model model, @PathVariable String pr_category) {
		
		model.addAttribute("categoryProductList", productService.getCategoryProductList(pr_category));
		
		return "product/categoryProductList";
	}
	
	@RequestMapping(value = "/search/result", method = RequestMethod.GET) 
	public String searchGet(@RequestParam("searchWord") String searchWord, Model model) {
		String searchWordNoSpace = searchWord.replaceAll("\\s", "");
		
		if (productService.searchCount(searchWord, searchWordNoSpace) == 0) {
			model.addAttribute("searchWord",searchWord);
			return "product/searchResultZero";
		} else {
			List<Product> productList = productService.search(searchWord, searchWordNoSpace);
			model.addAttribute("searchProductList", productList);
			return "product/searchResult";
		}
	}

}
