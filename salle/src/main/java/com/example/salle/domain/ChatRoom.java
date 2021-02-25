package com.example.salle.domain;

import java.sql.Timestamp;

import lombok.Data;


public @Data class ChatRoom {

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
	//not in DB
	private String content;
	private String sendTime;
	private String senderName;
	private String senderId;
	private String pr_title;
	
	public ChatRoom(int id, int pr_id, String sellerId, String buyerId, String fileName,
			Timestamp createdDate, String sellerName, String buyerName, int chatReadBuy, int chatReadSell) {
		super();
		this.id = id;
		this.pr_id = pr_id;
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.fileName = fileName;
		this.createdDate = createdDate;
		this.sellerName = sellerName;
		this.buyerName = buyerName;
		this.chatReadSell = chatReadSell;
	}

	public ChatRoom() {
		// TODO Auto-generated constructor stub
	}
	
	public ChatRoom(String content, String senderName, String sendTime, String senderId) {
		this.content = content;
		this.senderName = senderName;
		this.sendTime = sendTime;
		this.senderId = senderId;
	}
	
	
	
	
}
