package com.salle.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public @Data class ChatRoom {

	private String chatid;
	private int pr_id;
	private String sellerid;
	private String buyerid;
	private String pr_title;
	
	public ChatRoom(String chatid, int pr_id, String sellerid, String buyerid, String pr_title) {
		super();
		this.chatid = chatid;
		this.pr_id = pr_id;
		this.sellerid = sellerid;
		this.buyerid = buyerid;
		this.pr_title = pr_title;
	}
	
	public ChatRoom() {
		
	}
}
