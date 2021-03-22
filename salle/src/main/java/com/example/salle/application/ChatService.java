package com.example.salle.application;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.salle.domain.ChatMessage;
import com.example.salle.domain.ChatRoom;
import com.example.salle.domain.Login;
import com.example.salle.mapper.ChatMapper;

@Service
@Transactional
public class ChatService implements ChatMapper {
	
	private ChatMapper chatMapper;
	private ProductService productService;
	
	@Autowired
	public ChatService(ChatMapper chatMapper, ProductService productService) {
		this.chatMapper = chatMapper;
		this.productService = productService;
	}
	
	public void appendMessage(ChatMessage chatMessage) throws IOException {
		int pr_id = chatMessage.getPr_id();
		String chatid = "";
		String pr_email = productService.getProductInfo(pr_id).getPr_email();
		if (chatMessage.getFromid().equals(pr_email)) {
			chatid = pr_id + chatMessage.getToid();
		} else {
			chatid = pr_id + chatMessage.getFromid();
		}
		if (checkChatRoomExist(chatid) == 0) {
			String sellerid = pr_email;
			String pr_title = productService.getProductInfo(pr_id).getPr_title(); 
			String buyerid = chatMessage.getToid();
			ChatRoom chatRoom = new ChatRoom(chatid, pr_id, sellerid, buyerid, pr_title);
			addChatRoom(chatRoom);
		}//end chatRoom check If문
		chatMessage.setChatid(chatid);
		chatMapper.insertChatMessage(chatMessage);
	}

	public List<ChatMessage> readChatHistory(String chatid) {
		List<ChatMessage> chatHistory = chatMapper.getAllChatMessages(chatid);
		return chatHistory;
	}
	
	@Override
	public List<ChatMessage> getAllChatMessages(String chatid) {
		chatMapper.getAllChatMessages(chatid);
		return null;
	}
	
	@Override
	public void updateFileName(int id, String fileName) {

		chatMapper.updateFileName(id, fileName);
	}
	
	@Override
	public List<ChatRoom> getAllChatRoom(String email) {
		
		return chatMapper.getAllChatRoom(email);
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
	public void insertChatMessage(ChatMessage ChatMessage) {
		chatMapper.insertChatMessage(ChatMessage);
	}


	public ChatMessage infoSetting(HttpSession session, ChatMessage chatMessage, 
			List<ChatMessage> chatHistory) {
		int pr_id = chatMessage.getPr_id();
		Login loginInfo = (Login) session.getAttribute("login");
		String fromid = loginInfo.getEmail();
		String fromname = loginInfo.getNickName();
		chatMessage.setFromid(fromid);
		chatMessage.setFromname(fromname);	
		String chatid = pr_id + fromid; 
		chatMessage.setChatid(chatid);
		
		Object chatmessageExist = ObjectUtils.defaultIfNull(getAllChatMessages(chatid), 
				null);
		if (chatmessageExist != null) {
			chatHistory = readChatHistory(chatid);
		}
		return chatMessage;
	}

	@Override
	public void addChatRoom(ChatRoom chatMessage) {
		chatMapper.addChatRoom(chatMessage);
	}

	@Override
	public int checkChatRoomExist(String chatid) {
		return chatMapper.checkChatRoomExist(chatid);
	}

	@Override
	public ChatRoom getChatRoom(String chatid) {
		return chatMapper.getChatRoom(chatid);
	}

	@Override
	public ChatMessage getChatMessageInfo(String chatid) {
		return chatMapper.getChatMessageInfo(chatid);
	}

	public ChatMessage insertToInfo(ChatMessage chatMessageInfo, HttpSession session) {
		Login loginInfo = (Login) session.getAttribute("login");
		String myEmail = loginInfo.getEmail();
		chatMessageInfo.setToid("");
		String toid = chatMessageInfo.getToid();
		String fromid = chatMessageInfo.getFromid();
		if (!myEmail.equals(chatMessageInfo.getFromid())) {
			toid = chatMessageInfo.getFromid();
			fromid = chatMessageInfo.getToid();
		}
		String toname = productService.getNickNameByPrEmail(toid);
		chatMessageInfo.setFromid(fromid);
		chatMessageInfo.setToid(toid);
		chatMessageInfo.setToname(toname);
		return chatMessageInfo;
	}

}
