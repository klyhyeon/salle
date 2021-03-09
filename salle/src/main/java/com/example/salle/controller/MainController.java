package com.example.salle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.salle.application.ProductService;

@RestController
public class MainController {
	
	@Autowired
	ProductService productService;

    @GetMapping("/")
    public String home(Model model) {

		model.addAttribute("productList", productService.getProductList());
//		model.addAttribute("searchIcon", mainService.getPresignedUrl());

        return "main";
    }
}
