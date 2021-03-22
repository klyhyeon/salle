package com.example.salle.domain;

import lombok.Data;

public @Data class ChatMessage {

	private int pr_id;
	private String chatid;
	private String fromname;
	private String toname;
	private String fromid;
	private String toid;
	private String pr_title;
	private String chatmessage;
	private int chatread;
	private String pr_email;
	//DB에 없음
	public ChatMessage() {

	}
	
	public ChatMessage(String chatmessage, String fromname, String sendtime, String fromid) {
		this.chatmessage = chatmessage;
		this.fromname = fromname;
		this.fromid = fromid;
	}

	

	public ChatMessage(int pr_id, String toname, String toid, String pr_title) {
		super();
		this.pr_id = pr_id;
		this.toname = toname;
		this.toid = toid;
		this.pr_title = pr_title;
	}

	public ChatMessage(int pr_id, String chatid, String fromname, String toname, String fromid, String toid,
			String pr_title, String chatmessage, int chatread, String pr_email) {
		super();
		this.pr_id = pr_id;
		this.chatid = chatid;
		this.fromname = fromname;
		this.toname = toname;
		this.fromid = fromid;
		this.toid = toid;
		this.pr_title = pr_title;
		this.chatmessage = chatmessage;
		this.chatread = chatread;
		this.pr_email = pr_email;
	}

	
}
