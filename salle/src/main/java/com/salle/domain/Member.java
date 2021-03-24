package com.salle.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든 전역변수 포함시킨 생성자
public @Data class Member {

    private int id; //자동생성
    private String phoneNum;
    private String name;
    private String email;
    private String nickName;
    private String password;
    private String confirmPassword;
    private boolean emailDuplicate;
}
