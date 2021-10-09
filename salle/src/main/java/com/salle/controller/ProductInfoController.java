//package com.salle.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.salle.application.ProductService;
//import com.salle.domain.Product;
//
//@Controller
//public class ProductInfoController {
//
//	private ProductService productService;
//	private Product product;
//	private ChatMessage chatRoom;
//
//	@Autowired
//	public ProductInfoController(ProductService productService, Product product,
//			ChatMessage chatRoom) {
//		this.productService = productService;
//		this.product = product;
//		this.chatRoom = chatRoom;
//	}
//
//	//TODO: productInfo 뒤 seq 지정해야한다.
//	@RequestMapping(value = "/productInfo/{pr_id}", method = RequestMethod.GET)
//	public String productInfoGet(Model model, @PathVariable int pr_id) {
//
//		//.jsp에서 ${product.pr_title}
//		product = productService.getProductInfo(pr_id);
//		model.addAttribute("product", product);
//
//		List<String> productImgList = productService.getImgList(product);
//		model.addAttribute("productInfoImg", productImgList);
//		//member nickname
//		model.addAttribute("nickName",productService.getNickNameByPrEmail(product.getPr_email()));
//		//hoursfromupload
//		int hours = productService.getHoursFromUpload(product);
//		model.addAttribute("hoursFromUpload", hours);
//		return "product/productInfo";
//	}
//
//
//	@RequestMapping(value = "/productInfo/list", method = RequestMethod.GET)
//	public String productListGet(Model model) {
//		model.addAttribute("productListInfo", productService.getProductList());
//		return "product/productList";
//	}
//
//
//	@RequestMapping(value = "/category/{pr_category}", method = RequestMethod.GET)
//	public String productListGet(Model model, @PathVariable String pr_category) {
//		model.addAttribute("categoryProductList", productService.getCategoryProductList(pr_category));
//		return "product/categoryProductList";
//	}
//
//	@RequestMapping(value = "/search/result", method = RequestMethod.GET)
//	public String searchGet(@RequestParam("searchWord") String searchWord, Model model) {
//		String searchWordNoSpace = searchWord.replaceAll("\\s", "");
//
//		if (productService.searchCount(searchWord, searchWordNoSpace) == 0) {
//			model.addAttribute("searchWord",searchWord);
//			return "product/searchResultZero";
//		} else {
//			List<Product> productList = productService.search(searchWord, searchWordNoSpace);
//			model.addAttribute("searchProductList", productList);
//			return "product/searchResult";
//		}
//	}
//
//}
