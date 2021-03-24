package com.salle.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.salle.domain.Member;

@Mapper
public interface MemberMapper {
	
    void insertMember(Member member);

    Member memberInfo(String email);

}
