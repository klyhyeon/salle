package com.salle.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salle.application.ChatService;
import com.salle.application.MemberService;
import com.salle.application.ProductService;
import com.salle.domain.Login;
import com.salle.exception.IncorrectPasswordException;
import com.salle.exception.UnregisteredMemberException;

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
    public String loginHandle(@ModelAttribute("login") Login login, Model model) {

    	Login loginInfo;
    	
        try {
            loginInfo = memberService.loginMember(login);
        } catch (IncorrectPasswordException passwordException) {
        	model.addAttribute("login", null);
            return "login";
        } catch (UnregisteredMemberException memberException) {
        	model.addAttribute("login", null);
            return "login";
        }
        
//    		String email = login.getEmail();
//    		int messages = chatRoomService.getUnreadMessages(email);
//    		model.addAttribute("messageAlert", messages);    		
        
        model.addAttribute("productList", productService.getProductList());
        return "main";
    }

}
