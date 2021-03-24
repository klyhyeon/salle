package com.salle.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.domain.Product;
import com.salle.mapper.ProfileMapper;

@Transactional
@Service
public class ProfileService implements ProfileMapper {
	
    @Autowired
    ProfileMapper profileMapper;

	@Override
	public List<Product> getSellerProductList(String email) {

		return profileMapper.getSellerProductList(email);
	}

	@Override
	public int getTotalProduct(String email) {

		return profileMapper.getTotalProduct(email);
	}
	

}
