package com.salle.domain;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public @Data class ChatMessage {

	private int pr_id;
	private String sendtime;
	private String chatid;
	private String fromname;
	private String toname;
	private String fromid;
	private String toid;
	private String chatmessage;
	private int chatread;
	private String pr_email;

	public ChatMessage() {

	}
	
	public ChatMessage(int pr_id) {
		this.pr_id = pr_id;
	}
	
	public ChatMessage(String chatmessage, String fromname, String sendtime, String fromid) {
		this.chatmessage = chatmessage;
		this.fromname = fromname;
		this.sendtime = sendtime;
		this.fromid = fromid;
	}

	

	public ChatMessage(int pr_id, String toname, String toid) {
		super();
		this.pr_id = pr_id;
		this.toname = toname;
		this.toid = toid;
	}

	public ChatMessage(int pr_id, String sendtime, String fromname, String toname, String fromid, String toid,
			String chatmessage, int chatread, String chatid, String pr_email) {
		super();
		this.pr_id = pr_id;
		this.sendtime = sendtime;
		this.fromname = fromname;
		this.toname = toname;
		this.fromid = fromid;
		this.toid = toid;
		this.chatmessage = chatmessage;
		this.chatread = chatread;
		this.chatid = chatid;
		this.pr_email = pr_email;
	}

	
}
