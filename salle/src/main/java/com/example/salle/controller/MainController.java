package com.example.salle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.salle.application.AmazonS3Service;
import com.example.salle.application.ProductService;

@Controller
public class MainController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	AmazonS3Service amazonS3;

    @GetMapping("/")
    public String home(Model model) {

		model.addAttribute("productList", productService.getProductList());
		model.addAttribute("searchIcon", amazonS3.searchIcon());

        return "main";
    }
}
