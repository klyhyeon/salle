package com.salle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든 전역변수 포함시킨 생성자
public @Data class Login {

    private String email;
    private String password;
    private String nickName;
    
    public Login(String email, String password) {
    	this.email = email;
    	this.password = password;
    }

}