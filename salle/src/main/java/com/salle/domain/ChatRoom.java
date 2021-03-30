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
	private String pr_img_1;
	
	public ChatRoom() {
		
	}

	public ChatRoom(String chatid, int pr_id, String sellerid, String buyerid, String pr_title, String pr_img_1) {
		super();
		this.chatid = chatid;
		this.pr_id = pr_id;
		this.sellerid = sellerid;
		this.buyerid = buyerid;
		this.pr_title = pr_title;
		this.pr_img_1 = pr_img_1;
	}

}
