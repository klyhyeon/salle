package com.salle.application;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.salle.domain.Login;
import com.salle.domain.Member;
import com.salle.mapper.MemberMapper;

@Transactional
@Service
public class MemberService implements MemberMapper {
	
    MemberMapper memberMapper;
    
    public MemberService() {
    	
    }

    @Autowired
    public MemberService(MemberMapper memberMapper) {
    	this.memberMapper = memberMapper;
    }

    @Override
    public void insertMember(Member member) {
    	ConfigurablePasswordEncryptor encryptor = new ConfigurablePasswordEncryptor(); 
    	encryptor.setAlgorithm("MD5");
    	encryptor.setPlainDigest(true);
    	String rawPwd = member.getPassword();
    	String encryptedPwd = encryptor.encryptPassword(rawPwd);
    	member.setPassword(encryptedPwd);
        memberMapper.insertMember(member);
    }

    @Override
    public Member memberInfo(String email) {
        return memberMapper.memberInfo(email);
    }

    public Login loginMember(Login login, Errors errors) {
    	ConfigurablePasswordEncryptor encryptor = new ConfigurablePasswordEncryptor(); 
    	encryptor.setAlgorithm("MD5");
    	encryptor.setPlainDigest(true);
		Member memberInfoConvert = memberMapper.memberInfo(login.getEmail());
		boolean pwdVerify = false;
		if (memberInfoConvert != null) {
			pwdVerify = encryptor.checkPassword(login.getPassword(), memberInfoConvert.getPassword());
			login.setNickName(memberInfoConvert.getNickName()); 
		}
		checkEmail(memberInfoConvert, errors);
		checkPwd(pwdVerify, errors);
        return login;
    }
    
    public void checkPwd(boolean pwdVerify, Errors errors) {
        if (!pwdVerify) {
        	errors.rejectValue("password", "incorrect");
        }
    }
    
    public void checkEmail(Member memberInfo, Errors errors) {
        if (memberInfo == null)
        	errors.rejectValue("email", "unregistered");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"email","required");
    }

}
