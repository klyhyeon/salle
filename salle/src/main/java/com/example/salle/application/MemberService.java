package com.example.salle.application;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.salle.domain.Login;
import com.example.salle.domain.Member;
import com.example.salle.exception.IncorrectPasswordException;
import com.example.salle.exception.UnregisteredMemberException;
import com.example.salle.mapper.MemberMapper;

@Transactional
@Service
public class MemberService implements MemberMapper {
	
    @Autowired
    MemberMapper memberMapper;

    @Override
    public void insertMember(Member member) {
    	
        memberMapper.insertMember(member);
    }

    @Override
    public Member memberInfo(String email) {

        return memberMapper.memberInfo(email);
    }


    public Login loginMember(Login login) {

        Member memberInfo = memberMapper.memberInfo(login.getEmail());
                
        if (memberInfo == null) {
            throw new UnregisteredMemberException();
        } else {
            if (memberInfo.getPassword().equals(login.getPassword())) {
            } else {
                throw new IncorrectPasswordException();
            }
        }
        
        login.setNickName(memberInfo.getNickName()); 

        return login;
    }

}
