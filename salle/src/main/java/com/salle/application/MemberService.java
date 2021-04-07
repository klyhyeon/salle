package com.salle.application;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.salle.domain.Login;
import com.salle.domain.Member;
import com.salle.mapper.MemberMapper;
import com.salle.validation.LoginCheckEmptyAndEmailValidation;
import com.salle.validation.LoginCheckPwdValidation;

@Transactional
@Service
public class MemberService implements MemberMapper {
	
    MemberMapper memberMapper;

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
		Object memberInfo = ObjectUtils.defaultIfNull(memberMapper.memberInfo(login.getEmail()) , 
				null);
		Member memberInfoConvert = (Member) memberInfo;
		new LoginCheckEmptyAndEmailValidation().validate(login, errors);
		boolean checkPwd = encryptor.checkPassword(login.getPassword(), memberInfoConvert.getPassword());
		//new LoginCheckPwdValidation().validate(checkPwd, errors);
        login.setNickName(memberInfoConvert.getNickName()); 
        return login;
    }

}
