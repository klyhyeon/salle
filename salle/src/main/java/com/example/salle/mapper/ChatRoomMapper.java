package com.example.salle.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.salle.domain.ChatList;
import com.example.salle.domain.ChatRoom;

@Mapper
public interface ChatRoomMapper {
		
	public void addChatRoom (ChatRoom chatRoom) throws IOException;
	
	//String chatId, String pr_id, String senderId, String recipientId
	
	public List<ChatList> findByEmail(String email);
	
	public int countByChatId(int pr_id, String buyerId);
	
	public ChatRoom findByChatId(int pr_id, String buyerId);

	public void appendMessage(ChatRoom chatRoom) throws FileNotFoundException, IOException;

	public int getId(int pr_id, String buyerId);

	public void updateFileName(int id, String fileName);
	
	public void updateChatReadBuy(int id, int chatReadBuy);
	
	public void updateChatReadSell(int id, int chatReadSell);

	public int getUnreadMessages(String email);
	
	public List<Integer> getUnreadChatRoom(String email);

	public void insertChatMessage(String chatMessage, int id);

	public List<String> getAllChatMessages(int id);
}
