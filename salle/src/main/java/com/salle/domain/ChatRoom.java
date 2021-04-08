package com.salle.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public @Data class ChatRoom {

	private String chatid;
	private int pr_id;
	private String sellerid;
	private String buyerid;	
	
	public ChatRoom() {
		
	}

	public ChatRoom(String chatid, int pr_id, String sellerid, String buyerid) {
		super();
		this.chatid = chatid;
		this.pr_id = pr_id;
		this.sellerid = sellerid;
		this.buyerid = buyerid;
	}

}
