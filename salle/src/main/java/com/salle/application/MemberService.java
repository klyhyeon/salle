package com.salle.application;

import javax.transaction.Transactional;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.domain.Login;
import com.salle.domain.Member;
import com.salle.exception.IncorrectPasswordException;
import com.salle.exception.UnregisteredMemberException;
import com.salle.mapper.MemberMapper;

@Transactional
@Service
public class MemberService implements MemberMapper {
	
    @Autowired
    MemberMapper memberMapper;

    ConfigurablePasswordEncryptor encryptor = new ConfigurablePasswordEncryptor(); 

    @Override
    public void insertMember(Member member) {
    	encryptor.setAlgorithm("PBEWithMD5AndDES");
    	String rawPwd = member.getPassword();
    	String encryptedPwd = encryptor.encryptPassword(rawPwd);
    	member.setPassword(encryptedPwd);
        memberMapper.insertMember(member);
    }

    @Override
    public Member memberInfo(String email) {

        return memberMapper.memberInfo(email);
    }


    public Login loginMember(Login login) {
    	encryptor.setAlgorithm("PBEWithMD5AndDES");
        Member memberInfo = memberMapper.memberInfo(login.getEmail());
        boolean checkPwd = encryptor.checkPassword(login.getPassword(), memberInfo.getPassword());
                
        if (memberInfo == null) {
            throw new UnregisteredMemberException();
        } else {
            if (!checkPwd)
                throw new IncorrectPasswordException();
        }
        
        login.setNickName(memberInfo.getNickName()); 

        return login;
    }

}
