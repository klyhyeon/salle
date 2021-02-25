package com.example.salle.domain;

import java.sql.Timestamp;

import lombok.Data;

public @Data class ChatList {

	private int id;
	private int pr_id;
	private String sellerId;
	private String buyerId;
	private String fileName;
	private Timestamp createdDate;
	private String sellerName;
	private String buyerName;
	private int chatReadBuy;
	private int chatReadSell;
	private int chatRoomRead;
	//not in DB
	private String content;
	private String sendTime;
	private String senderName;
	private String pr_title;
	private String pr_img_1;

	public ChatList() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
