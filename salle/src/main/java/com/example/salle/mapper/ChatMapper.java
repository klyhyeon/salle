package com.example.salle.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.salle.domain.ChatMessage;
import com.example.salle.domain.ChatRoom;

@Mapper
public interface ChatMapper {
		
	public List<ChatRoom> getAllChatRoom(String email);
	
	public void appendMessage(ChatMessage chatRoom) throws FileNotFoundException, IOException;

	public int getId(int pr_id, String buyerId);

	public void updateFileName(int id, String fileName);
	
	public void updateChatReadQuery(int pr_id, String fromid, String toid);

	public int getUnreadMessages(String email);
	
	public List<Integer> getUnreadChatRoom(String email);

	public void insertChatMessage(ChatMessage chatmessage);

	public List<ChatMessage> getAllChatMessages(String chatid);
	
	public void addChatRoom(ChatRoom chatRoom);
	
	public int checkChatRoomExist(String chatid);
	
	public ChatRoom getChatRoom(String chatid);
	
	public ChatMessage getChatMessageInfo(String chatid);
}