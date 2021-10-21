package com.salle.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "CHAT_MESSAGE_LOG")
@Entity
public class ChatMessageLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
    private LocalDateTime deletedTime;
    private int sendStatus; //발송완료, 실패
    private int readStatus; //읽음 안읽음;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberChatRoom memberChatRoom;

}