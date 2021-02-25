package com.example.salle.application;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.salle.domain.Member;
import com.example.salle.domain.Product;
import com.example.salle.mapper.ProductMapper;

@Transactional
@Service
public class ProductService implements ProductMapper {
	
    @Autowired
    ProductMapper productMapper;
        
    private String namespace = "com.example.salle.mapper.ProductMapper";

    Timestamp tsSeller;
    Timestamp tsClient;
    
	@Override
	public void registerProduct(Product product) {
		
	   // Product product = new Product();
		
		//A date-time without a time-zone in the ISO-8601 calendar system,such as 2007-12-03T10:15:30.
		//2020-12-21 13:10:52.467 요런 형식임
        tsSeller = Timestamp.valueOf(LocalDateTime.now());
        
        product.setPr_reg_date(tsSeller);

        //Format을 내가 원하는대로 맞춰주기 위해 SimpleDateFormat을 활용해야하고 그래서 Date 객체를 써야함.
        //h2-consle의 parsedatetime을 쓸거기 때문에 아래 parsing은 생략해도 됨
//        Date date = new Date(ts.getTime());
//
//        System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
		
		//final TimestampWithTimeZone ts = new TimestampWithTimeZone(new Date().getTime(), 0, (short) 8);
		//https://www.codota.com/code/java/classes/org.h2.api.TimestampWithTimeZone
		
		productMapper.registerProduct(product);

	}

	@Override
	public int getCountProduct() {
		
		return productMapper.getCountProduct();
	}

	@Override
	public void deleteProduct(int pr_id) {
		productMapper.deleteProduct(pr_id);
	}

	@Override
	public List<Product> getProductList() {
		
		return productMapper.getProductList();
	}

	@Override
	public List<Product> getCategoryProductList(String pr_category) {

		return productMapper.getCategoryProductList(pr_category);
	}

	@Override
	public Product getProductInfo(int pr_id) {
		
		return productMapper.getProductInfo(pr_id);
	}

	@Override
	public String getMemberProductInfo(String email) {
		
		return productMapper.getMemberProductInfo(email);
	}

	@Override
	public List<Product> search(String searchWord, String searchWordNoSpace) {

		//String[] searchWordList = searchWord.split(" "); 
		return productMapper.search(searchWord, searchWordNoSpace);
	}

	@Override
	public int searchCount(String searchWord, String searchWordNoSpace) {

		return productMapper.searchCount(searchWord, searchWordNoSpace);
	}

	@Override
	public void updateProduct(Product product) {

		productMapper.updateProduct(product);
	}

//	@Override
//	public void deleteImg(int pr_id, String pr_img) {
//		
//		productMapper.deleteImg(pr_id, pr_img);
//	}

	@Override
	public void deleteImg1(int pr_id) {
		// TODO Auto-generated method stub
		productMapper.deleteImg1(pr_id);
		
	}

	@Override
	public void deleteImg2(int pr_id) {
		// TODO Auto-generated method stub
		productMapper.deleteImg2(pr_id);
		
	}

	@Override
	public void deleteImg3(int pr_id) {
		// TODO Auto-generated method stub
		productMapper.deleteImg3(pr_id);
		
	}

	@Override
	public void deleteImg4(int pr_id) {
		// TODO Auto-generated method stub
		
		productMapper.deleteImg4(pr_id);
	}

	@Override
	public void deleteImg5(int pr_id) {
		// TODO Auto-generated method stub
		
		productMapper.deleteImg5(pr_id);
	}




}
