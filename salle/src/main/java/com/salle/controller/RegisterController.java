package com.salle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salle.domain.Member;
import com.salle.mapper.MemberMapper;
import com.salle.validation.RegisterValidation;


@Validated
@Controller
public class RegisterController {
	
	  @Autowired
	  MemberMapper memberService;
  
    //회원가입 페이지 노출
    @RequestMapping(value = "/register/main", method = RequestMethod.GET)
    public String registerAttempt(@ModelAttribute("member") Member member) {

        return "register/main";
    }

    @RequestMapping(value = "/register/done", method = RequestMethod.POST)
    public String registerHandle(@ModelAttribute("member") Member member, 
    		Errors errors) {		
    		if (memberService.memberInfo(member.getEmail()) != null) {
    			member.setEmailDuplicate(true);
    		}
    
    		new RegisterValidation().validate(member, errors);
    		
    		if (errors.hasErrors())
    			return "register/main";
    
    	
	        memberService.insertMember(member);
	        return "register/done";
	}
}
