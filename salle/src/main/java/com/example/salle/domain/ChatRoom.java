package com.example.salle.domain;

import lombok.Data;

public @Data class ChatRoom {

	private int id;
	private int pr_id;
	private String sendtime;
	private String fromid;
	private String toid;
	private String fromname;
	private String toname;
	private String chatmessage;
	private int chatread;
	//not in DB
	private String pr_title;

	
	public ChatRoom(int id, int pr_id, String fromid, String toid,
			String sendtime, String fromname, String toname, int chatReadBuy, int chatread) {
		super();
		this.id = id;
		this.pr_id = pr_id;
		this.fromid = fromid;
		this.toid = toid;
		this.sendtime = sendtime;
		this.fromname = fromname;
		this.toname = toname;
		this.chatread = chatread;
	}
	
	public ChatRoom() {

	}
	
	public ChatRoom(String chatmessage, String fromname, String sendtime, String fromid) {
		this.chatmessage = chatmessage;
		this.fromname = fromname;
		this.sendtime = sendtime;
		this.fromid = fromid;
	}
	
}
