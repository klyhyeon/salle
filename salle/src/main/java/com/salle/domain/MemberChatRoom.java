package com.salle.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MEMBER_CHAT_ROOM")
public class MemberChatRoom {

    @Id @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memberChatRoom")
    private List<ChatMessageLog> chatMessageLogs;

}