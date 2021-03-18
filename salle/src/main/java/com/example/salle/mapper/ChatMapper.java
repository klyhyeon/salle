package com.example.salle.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.salle.domain.ChatList;
import com.example.salle.domain.Chatmessage;

@Mapper
public interface ChatMapper {
		
	public List<ChatList> getAllChatRoom(String email);
	
	public Chatmessage findByChatId(int pr_id, String buyerId);

	public void appendMessage(Chatmessage chatRoom) throws FileNotFoundException, IOException;

	public int getId(int pr_id, String buyerId);

	public void updateFileName(int id, String fileName);
	
	public void updateChatReadQuery(int pr_id, String fromid, String toid);

	public int getUnreadMessages(String email);
	
	public List<Integer> getUnreadChatRoom(String email);

	public void insertChatMessage(Chatmessage chatmessage);

	public List<Chatmessage> getAllChatMessages(int pr_id, String fromid);
}
