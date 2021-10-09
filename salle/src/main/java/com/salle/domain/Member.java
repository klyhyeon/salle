package com.salle.domain;

`import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int id; //자동생성
    private String phone;
    @Column(name = "member_name")
    private String name;
    private String email;
    private String nickName;
    private String password;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private LocalDateTime deletedTime;
    private int status; //휴면, 탈퇴, 활동

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Product> products;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<MemberChatRoom> memberChatRooms;
}
