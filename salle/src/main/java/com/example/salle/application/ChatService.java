package com.example.salle.application;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.salle.domain.ChatList;
import com.example.salle.domain.Chatmessage;
import com.example.salle.domain.Login;
import com.example.salle.mapper.ChatMapper;

@Service
@Transactional
public class ChatService implements ChatMapper {
	
	private ChatMapper chatMapper;
	
	@Autowired
	public ChatService(ChatMapper chatMapper) {
		this.chatMapper = chatMapper;
	}
	
	public void appendMessage(Chatmessage chatmessage) throws IOException {
		chatMapper.insertChatMessage(chatmessage);
	}

	public List<Chatmessage> readChatHistory(String chatid) throws IOException {
		List<Chatmessage> chatHistory = chatMapper.getAllChatMessages(chatid);
		return chatHistory;
	}
	
	@Override
	public void updateFileName(int id, String fileName) {

		chatMapper.updateFileName(id, fileName);
	}
	
	@Override
	public List<ChatList> getAllChatRoom(String email) {
		
		return chatMapper.getAllChatRoom(email);
	}
	

	@Override
	public Chatmessage findByChatId(int pr_id, String buyerId) {
		return chatMapper.findByChatId(pr_id, buyerId);
	}
	
	@Override
	public int getId(int pr_id, String buyerId) {
		
		return chatMapper.getId(pr_id, buyerId);
	}
	
	@Override
	public int getUnreadMessages(String email) {
		return chatMapper.getUnreadMessages(email);
	}

	@Override
	public List<Integer> getUnreadChatRoom(String email) {
		List<Integer> unread = chatMapper.getUnreadChatRoom(email); 
		return unread;
	}

	public void updateChatRead(String json) {
		JSONObject jsn = new JSONObject(json);
		int pr_id = (int) jsn.get("pr_id");
		String fromid = (String) jsn.get("fromid");
		String toid = (String) jsn.get("toid");
		chatMapper.updateChatReadQuery(pr_id, fromid, toid);
	}

	@Override
	public void updateChatReadQuery(int pr_id, String fromid, String toid) {
		chatMapper.updateChatReadQuery(pr_id, fromid, toid);
	}

	@Override
	public void insertChatMessage(Chatmessage chatmessage) {
		chatMapper.insertChatMessage(chatmessage);
	}

	@Override
	public List<Chatmessage> getAllChatMessages(int pr_id, String fromid) {
		chatMapper.getAllChatMessages(pr_id, fromid);
		return null;
	}

	public Chatmessage infoSetting(HttpSession session, Chatmessage chatmessage) {
		Login loginInfo = (Login) session.getAttribute("login");
		String fromid = loginInfo.getEmail();
		String fromname = loginInfo.getNickName();
		chatmessage.setFromid(fromid);
		chatmessage.setFromname(fromname);	
		return chatmessage;
	}


	

	
	

}
