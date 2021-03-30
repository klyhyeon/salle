package com.salle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.salle.domain.ChatMessage;
import com.salle.domain.ChatRoom;

@Mapper
public interface ChatMapper {
		
	public List<ChatRoom> getAllChatRoom(String email);
	
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

	public String getHeadChatMessage(String chatid);
}
