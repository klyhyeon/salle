package com.example.salle.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.salle.application.ProductEditService;
import com.example.salle.application.ProductService;
import com.example.salle.domain.Product;
import com.example.salle.domain.UuidImgname;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductEditController {
	
	@Autowired
    ProductService productService;
	
	@Autowired
	ProductEditService productEditService;
	
	@Autowired
	UuidImgname uuidImgname;
    
    //상품등록 이미지파일 업로드
    Product product_file = new Product();
    
    @Value("${cloud.aws.s3.bucket}")
    String bucket; 

	//profile에서 판매글 수정, 삭제하기
	@RequestMapping(value= "/product/{pr_id}/edit", method = RequestMethod.GET)
	public String productEdit(Model model, @PathVariable int pr_id) {
		List<String> imgList = new ArrayList<String>();
		Product product = productEditService.productEdit(pr_id, imgList);
		model.addAttribute("product", product);
		model.addAttribute("imgList", imgList);
		imgList = null;
		return "product/productEdit"; 
	}
	

	Product productUpdate = new Product();
	int exImgCnt = 0;
    @RequestMapping(value= "/productEdit/ajax", method= RequestMethod.POST)
    public void productEdit(HttpServletRequest req) throws Exception {
    	//exImgCnt = productEditService.imgEdit(json, productUpdate, bucket);
    	String pr_id = req.getParameter("pr_id");
    	String[] exImgArr = req.getParameterValues("imgExArr");
    	System.out.println(exImgArr[0]);
    	MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) req;
    	Iterator<String> itr = multiReq.getFileNames();
    	MultipartFile multiFile = null;
    	while(itr.hasNext()) {
    		multiFile = multiReq.getFile(itr.next());
    		String tmpName = multiFile.getOriginalFilename();
    		//System.out.println(tmpName);
    	}
    }

    @RequestMapping(value= "/productEditImg/ajax", method= RequestMethod.POST)
    public void productEditImg(HttpServletRequest req) throws Exception {
    	productEditService.imgEditUpload(req, productUpdate, bucket, exImgCnt);
    }
	
    
	@RequestMapping(value= "/product/{pr_id}/save", method= RequestMethod.POST)
	public String profileEditDone(@ModelAttribute("product") Product product, Errors errors,
			HttpSession httpSession, @PathVariable("pr_id") int pr_id) {
		productEditService.productSave(product, productUpdate, httpSession, errors);
		
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
