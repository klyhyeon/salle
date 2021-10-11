package com.salle.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CHAT_ROOM")
public class ChatRoom {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	@OneToMany(mappedBy = "chatRoom")
	private List<MemberChatRoom> memberChatRooms;

	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMessageLog> chatMessageLogs;

	public void setChatMessageLogs(List<ChatMessageLog> chatMessageLogs) {
		this.chatMessageLogs = chatMessageLogs;
	}

	public List<ChatMessageLog> getChatMessageLogs() {
		return chatMessageLogs;
	}

}
