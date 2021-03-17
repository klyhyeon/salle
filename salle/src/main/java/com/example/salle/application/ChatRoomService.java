package com.example.salle.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.salle.domain.ChatList;
import com.example.salle.domain.ChatRoom;
import com.example.salle.mapper.ChatRoomMapper;

@Service
@Transactional
public class ChatRoomService implements ChatRoomMapper {
	
	private ChatRoomMapper chatRoomMapper;
	
	@Autowired
	public ChatRoomService(ChatRoomMapper chatRoomMapper) {
		this.chatRoomMapper = chatRoomMapper;
	}
	
	//StringBuilder or BufferedReader or BufferedWriter 사용해서 불러오기
	public List<ChatRoom> readChatHistory(int pr_id, String fromid) throws IOException {
		//View에 ChatRoom 객체로 전달
		List<ChatRoom> chatHistory = chatRoomMapper.getAllChatMessages(pr_id, fromid);
		return chatHistory;
	}
	
	@Override
	public void updateFileName(int id, String fileName) {

		chatRoomMapper.updateFileName(id, fileName);
	}
	
	@Override
	public List<ChatList> findByEmail(String email) {
		
		return chatRoomMapper.findByEmail(email);
	}
	
	//no connection with DB
	public void appendMessage(ChatRoom chatRoom) throws IOException {
		String chatmessage = chatRoom.getChatmessage();
		String fromid = chatRoom.getFromid();
		String toid = chatRoom.getToid();
		System.out.println("print:" + chatmessage);
		//String content = senderName + "\n" + chatmessage + "\n" + "[" +  sendTime + "]" + "\n";
		chatRoomMapper.insertChatMessage(chatmessage, fromid, toid);
	}

	@Override
	public ChatRoom findByChatId(int pr_id, String buyerId) {
		return chatRoomMapper.findByChatId(pr_id, buyerId);
	}
	
	@Override
	public int getId(int pr_id, String buyerId) {
		
		return chatRoomMapper.getId(pr_id, buyerId);
	}
	
	@Override
	public int getUnreadMessages(String email) {
		return chatRoomMapper.getUnreadMessages(email);
	}

	@Override
	public List<Integer> getUnreadChatRoom(String email) {
		List<Integer> unread = chatRoomMapper.getUnreadChatRoom(email); 
		return unread;
	}

	@Override
	public void insertChatMessage(String chatMessage, String fromid, String toid) {
		chatRoomMapper.insertChatMessage(chatMessage, fromid, toid);
	}
	
	@Override
	public List<String> getAllChatMessages(int id) {
		return chatRoomMapper.getAllChatMessages(id);
	}

	public void updateChatRead(String json) {
		JSONObject jsn = new JSONObject(json);
		int pr_id = (int) jsn.get("pr_id");
		String fromid = (String) jsn.get("fromid");
		String toid = (String) jsn.get("toid");
		chatRoomMapper.updateChatReadQuery(pr_id, fromid, toid);
	}

	@Override
	public void updateChatReadQuery(int pr_id, String fromid, String toid) {
		chatRoomMapper.updateChatReadQuery(pr_id, fromid, toid);
	}


	

	
	

}
