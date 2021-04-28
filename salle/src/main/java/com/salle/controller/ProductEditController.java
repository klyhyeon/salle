package com.salle.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
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

import com.salle.application.ProductEditService;
import com.salle.application.ProductService;
import com.salle.domain.Product;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductEditController {
	
    private ProductService productService;	
    private ProductEditService productEditService;
    private Product productUpdate = new Product();

	@Autowired
	public ProductEditController(ProductService productService, ProductEditService productEditService) {
		this.productService = productService;
		this.productEditService = productEditService;
	}
    
    //상품등록 이미지파일 업로드
    Product product_file = new Product();
    
    @Value("${cloud.aws.s3.bucket}")
    String bucket; 

	//profile에서 판매글 수정, 삭제하기
	@RequestMapping(value= "/product/{pr_id}/edit", method = RequestMethod.GET)
	public String productEdit(Model model, @PathVariable int pr_id) {
		Product product = productService.getProductInfo(pr_id);
		model.addAttribute("product", product);
		return "product/productEdit"; 
	}
	
	@RequestMapping(value= "/get/imgList/{pr_idStr}", method= RequestMethod.GET)
	public JSONArray sendImgList(@PathVariable("pr_idStr") String pr_idStr) {
		int pr_id = Integer.parseInt(pr_idStr);
		JSONArray jsnImgList = productEditService.getImgList(pr_id);
		return jsnImgList;
	}

	
	int exImgCnt = 0;
    @RequestMapping(value= "/productEdit/ajax", method= RequestMethod.POST)
    public Product productEdit(HttpServletRequest req) throws Exception {
    	productUpdate = productEditService.imgEdit(req, productUpdate, bucket);
    	return productUpdate;
    }
	
    
	@RequestMapping(value= "/product/{pr_id}/save", method= RequestMethod.POST)
	public String profileEditDone(@ModelAttribute("product") Product product, Errors errors,
			HttpSession httpSession, @PathVariable("pr_id") int pr_id) {
		product = productEditService.productSave(product, productUpdate, httpSession, errors);
		
		if (errors.hasErrors())
			return "product/productEdit";
		
		productService.updateProduct(product);
		return "redirect:/productInfo/" + product.getPr_id();
	} 
    	
	
	String flag;	
	@RequestMapping(value= "/product/{pr_id}/delete", method= RequestMethod.GET)
	public String profileDelete(Model model, @PathVariable int pr_id) throws UnsupportedEncodingException {	
		String nickName = productEditService.productDelete(pr_id);

		if (flag.equals("true"))
			productService.deleteProduct(pr_id);
		
		return "redirect:/profile/" + nickName;
	}
	
	
	@RequestMapping(value= "/productDelete/ajax", method=RequestMethod.POST)
	public void ajaxDelete(@RequestBody String json) {
		JSONObject jsn = new JSONObject(json);
		flag = (String) jsn.get("flag");
	}
}
