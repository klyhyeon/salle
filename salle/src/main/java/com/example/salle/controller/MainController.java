package com.example.salle.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.salle.application.ChatRoomService;
import com.example.salle.application.MainService;
import com.example.salle.application.ProductService;

@Controller
public class MainController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
    ChatRoomService chatRoomService;
	
	@Autowired
	MainService mainService;
	
    @GetMapping("/")
    public String home(Model model, HttpSession httpSession) {

		model.addAttribute("productList", productService.getProductList());
//		model.addAttribute("searchIcon", mainService.getPresignedUrl());

        return "main";
    }

}
