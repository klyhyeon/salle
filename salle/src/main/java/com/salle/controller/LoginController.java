package com.salle.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salle.application.ChatService;
import com.salle.application.MemberService;
import com.salle.application.ProductService;

@Controller
public class LoginController {
	
    @Autowired
    MemberService memberService;
    
    @Autowired
    ProductService productService;
    
    @Autowired
    ChatService chatRoomService;

    //회원가입 페이지 노출
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginAttempt(@ModelAttribute("login") Login login) {
        return "login";
    }

    @RequestMapping(value = "/login/done", method = RequestMethod.POST)
    public String loginHandle(@Valid Login login, 
    		Model model, Errors errors) {
    	
    	memberService.loginMember(login, errors);
    	if (errors.hasErrors())
    		return "/login";
        model.addAttribute("productList", productService.getProductList());
        return "main";
    }

}
