package com.example.salle.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.salle.domain.ChatList;
import com.example.salle.domain.ChatRoom;

@Mapper
public interface ChatRoomMapper {
		
	public List<ChatList> findByEmail(String email);
	
	public ChatRoom findByChatId(int pr_id, String buyerId);

	public void appendMessage(ChatRoom chatRoom) throws FileNotFoundException, IOException;

	public int getId(int pr_id, String buyerId);

	public void updateFileName(int id, String fileName);
	
	public void updateChatReadQuery(int pr_id, String fromid, String toid);

	public int getUnreadMessages(String email);
	
	public List<Integer> getUnreadChatRoom(String email);

	public void insertChatMessage(String chatMessage, String fromid, String toid);

	public List<ChatRoom> getAllChatMessages(int pr_id, String fromid);
}
