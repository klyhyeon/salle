package com.example.salle.domain;

import lombok.Data;

public @Data class Chatmessage {

	private int id;
	private int pr_id;
	private String sendtime;
	private String fromname;
	private String toname;
	private String fromid;
	private String toid;
	private String pr_title;
	private String chatmessage;
	private int chatread;

	
	public Chatmessage() {

	}
	
	public Chatmessage(String chatmessage, String fromname, String sendtime, String fromid) {
		this.chatmessage = chatmessage;
		this.fromname = fromname;
		this.sendtime = sendtime;
		this.fromid = fromid;
	}

	public Chatmessage(int id, int pr_id, String sendtime, String fromname, String toname, String fromid, String toid,
			String pr_title, String chatmessage, int chatread) {
		super();
		this.id = id;
		this.pr_id = pr_id;
		this.sendtime = sendtime;
		this.fromname = fromname;
		this.toname = toname;
		this.fromid = fromid;
		this.toid = toid;
		this.pr_title = pr_title;
		this.chatmessage = chatmessage;
		this.chatread = chatread;
	}
	
}
