package com.salle.application;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salle.domain.ChatMessage;
import com.salle.domain.ChatRoom;
import com.salle.domain.Login;
import com.salle.domain.Product;
import com.salle.mapper.ChatMapper;

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
	
	public ChatMessage appendMessage(ChatMessage chatMessage) throws IOException {
		int pr_id = chatMessage.getPr_id();
		String chatid = "";
		Product product = productService.getProductInfo(pr_id);
		String pr_email = product.getPr_email();
		String buyerid = "";
		String sellerid = "";				
		if (chatMessage.getFromid().equals(pr_email)) {
			chatid = pr_id + chatMessage.getToid();
			buyerid = chatMessage.getToid();
			sellerid = chatMessage.getFromid();
		} else {
			chatid = pr_id + chatMessage.getFromid();
			buyerid = chatMessage.getFromid();
			sellerid = chatMessage.getToid();
		}
		if (checkChatRoomExist(chatid) == 0) {
			ChatRoom chatRoom = new ChatRoom(chatid, pr_id, sellerid, buyerid);
			addChatRoom(chatRoom);
		}//end chatRoom check If문
		chatMessage.setChatid(chatid);
		chatMessage.setPr_email(pr_email);
		chatMapper.insertChatMessage(chatMessage);
		return chatMessage;
	}

	public List<ChatMessage> readChatHistory(String chatid) {
		return chatMapper.getAllChatMessages(chatid);
	}
	
	@Override
	public List<ChatMessage> getAllChatMessages(String chatid) {
		
		return chatMapper.getAllChatMessages(chatid);
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

	public ChatMessage infoSetting(HttpSession session, ChatMessage chatMessage) {
		int pr_id = chatMessage.getPr_id();
		Product product = productService.getProductInfo(pr_id);
		String pr_email = product.getPr_email();
		chatMessage.setPr_email(pr_email);
		Login loginInfo = (Login) session.getAttribute("login");
		String fromid = loginInfo.getEmail();
		String fromname = loginInfo.getNickName();
		chatMessage.setFromid(fromid);
		chatMessage.setFromname(fromname);	
		String chatid = pr_id + fromid;
		chatMessage.setChatid(chatid);
		chatMessage.setPr_id(pr_id);
		chatMessage.setToid(pr_email);
		chatMessage.setToname(productService.getNickNameByPrEmail(pr_email));
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
	public ChatMessage getChatMessageInfoByChatid(String chatid) {
		return chatMapper.getChatMessageInfoByChatid(chatid);
	}

	public ChatMessage insertToInfo(ChatMessage chatMessageInfo, HttpSession session) {
		Login loginInfo = (Login) session.getAttribute("login");
		String myEmail = loginInfo.getEmail();
		String toid = "";
		if (!myEmail.equals(chatMessageInfo.getFromid())) {
			toid = chatMessageInfo.getFromid();
		}
		String toname = productService.getNickNameByPrEmail(toid);
		String fromname = productService.getNickNameByPrEmail(myEmail);
		chatMessageInfo.setToid(toid);
		chatMessageInfo.setToname(toname);
		chatMessageInfo.setFromid(myEmail);
		chatMessageInfo.setFromname(fromname);
		return chatMessageInfo;
	}

	@Override
	public String getHeadChatMessage(String chatid) {
		return chatMapper.getHeadChatMessage(chatid);
	}

	public String chatRoomJsonArr(List<ChatRoom> chatRoomList, String email) {
		ChatMessage chatMessageInfo = getChatMessageInfoByEmail(email);
		String username = "";
		if (chatMessageInfo.getFromid().equals(email)) {
			username = chatMessageInfo.getToname();
		} else {
			username = chatMessageInfo.getFromname();
		}
		JSONArray jsnArr = new JSONArray();
		for (ChatRoom chatRoom : chatRoomList) {
			 JSONObject jsnObj = new JSONObject(chatRoom);
			 Product product = productService.getProductInfo(chatRoom.getPr_id());
			 jsnObj.put("pr_img_1", product.getPr_img_1());
			 jsnObj.put("pr_title", product.getPr_title());
			 jsnObj.put("username", username);
			 jsnObj.put("chatid", chatRoom.getChatid());
			 jsnObj.put("pr_id", chatRoom.getPr_id());
			 jsnArr.put(jsnObj);
		}
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", jsnArr);
		 String result = jsnResult.toString();
		 System.out.println("chatResult toString: " + result);
		return result;
	}

	public String chatMessageJson(List<ChatRoom> chatRoomList, String email) {
		JSONArray jsnArr = new JSONArray();
		for (ChatRoom chatList : chatRoomList) {
			 String message = getHeadChatMessage(chatList.getChatid());
			 JSONObject jo = new JSONObject();
			 jo.put("message", message);
			 jsnArr.put(jo);
		}
//		 if (unreadChatId.size() == 0) {
//			 jo.put("messageUnread", "");
//		 	} else {
//		 		//읽지 않은 chatmessageId들과 현재 chatmessageId 대조 후 처리 
//				 for (int ele : unreadChatId) {
//					 	if (chatList.getId() == ele) {
//					 		jo.put("messageUnread", "새 메세지");
//					 		break;
//					 	} else {
//					 		jo.put("messageUnread", "");
//					 	}
//				 }
//			}
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", jsnArr);
		 String result = jsnResult.toString();
		return result;
	}

	@Override
	public ChatMessage getChatMessageInfoByEmail(String email) {
		return chatMapper.getChatMessageInfoByEmail(email);
	}

}
