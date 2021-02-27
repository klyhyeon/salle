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
	
	
	@RequestMapping(value="/ajax/img/delete", method=RequestMethod.POST)
	public void ajaxDeleteImg(@RequestBody String json) {
    	System.out.println("imgDelete in process");
    	
    	//TODO: 실제 파일 delete

		JSONObject jsn = new JSONObject(json);
		String pr_img_tmp = (String) jsn.get("pr_img");
		String index = pr_img_tmp.substring(7);
		int indexInt = Integer.parseInt(index) + 1;
		//String pr_img_delete = "pr_img_" + indexInt;
		//System.out.println("json pr_img: " + pr_img_delete);
		
		String pr_id_str = (String) jsn.get("pr_id");
		int pr_id = Integer.parseInt(pr_id_str);
		System.out.println("json pr_id: " + pr_id);
		
    	productTemp = productService.getProductInfo(pr_id);
    	
    	String imgFileName = "";
    	
   	 	switch (indexInt) {
		
		case 1:
			productService.deleteImg1(pr_id);
			imgFileName = productTemp.getPr_img_1();
			break;
		case 2:
			productService.deleteImg2(pr_id);
			imgFileName = productTemp.getPr_img_2();
			break;
		case 3:
			productService.deleteImg3(pr_id);
			imgFileName = productTemp.getPr_img_3();
			break;
		case 4:
			productService.deleteImg4(pr_id);
			imgFileName = productTemp.getPr_img_4();
			break;
		case 5:
			productService.deleteImg5(pr_id);
			imgFileName = productTemp.getPr_img_5();
			break;

		default:
			break;
		}
		//productService.deleteImg(pr_id, pr_img_delete);
   	 	//TODO: delete imgFileName from S3
   	 	amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, imgFileName));
   	 	
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
    	
    	//DB pr_img_ 파일이 어디까지 차있는지 봐야함
    	productTemp = productService.getProductInfo(pr_id);
    	String[] imgArr = new String[5];
    	imgArr[0] = productTemp.getPr_img_1();
    	imgArr[1] = productTemp.getPr_img_2();
    	imgArr[2] = productTemp.getPr_img_3();
    	imgArr[3] = productTemp.getPr_img_4();
    	imgArr[4] = productTemp.getPr_img_5();
    	
    	//TODO: 기존 S3 img파일들을 모두 지워줌
    	//		다시 올린 파일과 이전 파일이 동일한 지 검증하기 어려움
    	for (int)


    	int idxEmpty;
    	
    	for (int i = 0; i < 5; i++) {
    		if (imgArr[i] == null) {
    			idxEmpty = i; 
    			break;
    		}
    	}
    	
    	System.out.println("idxEmpty: " + idxEmpty);
    	
    	switch (idxEmpty) {
    	case 0:
    		product.setPr_img_1(imgArr[0]);
    		product.setPr_img_2(imgArr[1]);
    		product.setPr_img_3(imgArr[2]);
    		product.setPr_img_4(imgArr[3]);
    		product.setPr_img_5(imgArr[4]);			
    		break;
		case 1:
			product.setPr_img_1(product_file_edit.getPr_img_1());
			product.setPr_img_2(product_file_edit.getPr_img_2());
			product.setPr_img_3(product_file_edit.getPr_img_3());
			product.setPr_img_4(product_file_edit.getPr_img_4());
			product.setPr_img_5(product_file_edit.getPr_img_5());			
			break;
		case 2:
			product.setPr_img_1(img1);
			product.setPr_img_2(product_file_edit.getPr_img_1());
			product.setPr_img_3(product_file_edit.getPr_img_2());
			product.setPr_img_4(product_file_edit.getPr_img_3());
			product.setPr_img_5(product_file_edit.getPr_img_4());			
			break;
		case 3:
			product.setPr_img_1(img1);
			product.setPr_img_2(img2);
			product.setPr_img_3(product_file_edit.getPr_img_1());
			product.setPr_img_4(product_file_edit.getPr_img_2());
			product.setPr_img_5(product_file_edit.getPr_img_3());			
			break;
		case 4:
			product.setPr_img_1(img1);
			product.setPr_img_2(img2);
			product.setPr_img_3(img3);
			product.setPr_img_4(product_file_edit.getPr_img_1());
			product.setPr_img_5(product_file_edit.getPr_img_2());			
			break;
		case 5:
			product.setPr_img_1(img1);
			product.setPr_img_2(img2);
			product.setPr_img_3(img3);
			product.setPr_img_3(img4);
			product.setPr_img_5(product_file_edit.getPr_img_1());			
			break;

		default:
			break;
		}
    	
		//ajax로 받은 img_file 정보를 넘겨줌 
    	
		new SellProductValidation().validate(product, errors);
		
		if (errors.hasErrors()) {
			return "product/productEdit";
		}
		
		productService.updateProduct(product);
		
		return "product/productInfo";
	}
	
	   //상품수정 이미지파일 업로드 
    Product product_file_edit = new Product(); 
    
    @RequestMapping(value= "/sell/ajax/edit", method= RequestMethod.POST)
    public void ajaxEdit(HttpServletRequest req) throws Exception {
    	
    	System.out.println("imgAdd in process");
    	
    	
    	//formdata를 받은 req를 multipartfile로 타입 변환해줌
    	MultipartHttpServletRequest multi = (MultipartHttpServletRequest) req;
    	Iterator<String> iterator = multi.getFileNames(); 	
    	MultipartFile multipartFile = null;
    	
    	int reps = 0;
    	
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
    			product_file_edit.setPr_img_1(fileName);
    			break;
    		case 1: 
    			product_file_edit.setPr_img_2(fileName);
    			break;
    		case 2: 
    			product_file_edit.setPr_img_3(fileName);
    			break;
    		case 3: 
    			product_file_edit.setPr_img_4(fileName);
    			break;
    		case 4: 
    			product_file_edit.setPr_img_5(fileName);
    			break;
    		}
    		
    		
    		reps++;
    	}
    	System.out.println("imgAdd: " + product_file_edit.getPr_img_1());
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
