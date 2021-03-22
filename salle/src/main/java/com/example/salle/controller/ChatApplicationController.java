package com.example.salle.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.salle.application.ChatService;
import com.example.salle.application.ProductService;
import com.example.salle.domain.ChatList;
import com.example.salle.domain.ChatMessage;
import com.example.salle.domain.ChatRoom;
import com.example.salle.domain.Product;

@Controller
public class ChatApplicationController {
	
	private SimpMessagingTemplate simpMessageTemplate;
	private ChatService chatService;
	private ProductService productService;
	
	@Autowired
	public ChatApplicationController(SimpMessagingTemplate simpMessagingTemplate, 
			ChatService chatService, ProductService productService) {
		this.simpMessageTemplate = simpMessagingTemplate;
		this.chatService = chatService;
		this.productService = productService;
	}

	@RequestMapping(value="/product/chatStart", method=RequestMethod.GET)
	public String productChatMessage(Model model, HttpSession session, 
			@ModelAttribute("chatmessage") ChatMessage chatMessage) throws IOException {
		
		List<ChatMessage> chatHistory = null;
		ChatMessage chatMessageInfo = chatService.infoSetting(session, chatMessage, 
											chatHistory);
		model.addAttribute("chatMessageInfo", chatMessageInfo);
		if (chatHistory != null) 
			model.addAttribute("chatHistory", chatHistory);
		return "chat/chatmessage";
	}
	
	@RequestMapping(value="/getChatList/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatList(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
		List<ChatRoom> chatRoomList = chatService.getAllChatRoom(email);		 
		JSONArray jsnArr = new JSONArray();
		for (ChatRoom chatRoom : chatRoomList) {
			 JSONObject jsnObj = new JSONObject();
			 Product product = productService.getProductInfo(chatRoom.getPr_id());
			 jsnObj.put("pr_img_1", product.getPr_img_1());
			 jsnArr.put(jsnObj);
		}
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", jsnArr);
		 String result = jsnResult.toString();
		 System.out.println("chatResult toString: " + result);
		 return result;
	}
	
	@RequestMapping(value="/chatList/chatStart", method=RequestMethod.GET)
	public String getchatmessage(Model model, HttpServletRequest req, HttpSession session) throws IOException {
		String chatid = req.getParameter("chatid");
		List<ChatMessage> chatHistory = chatService.readChatHistory(chatid);
		model.addAttribute("chatHistory", chatHistory);
		
		ChatMessage chatMessageInfo = chatService.getChatMessageInfo(chatid);
		chatMessageInfo = chatService.insertToInfo(chatMessageInfo, session);
		model.addAttribute("chatMessageInfo", chatMessageInfo);
		return "chat/chatmessage";
	}
	
	@MessageMapping("/broadcast")
	public void send(ChatMessage chatmessage) throws IOException {
		chatService.appendMessage(chatmessage);
		String chatid = chatmessage.getChatid();
		//TODO: sendTime 어떻게 반환해줄건지
		String urlSubscribe = "/user/" + chatid + "/queue/messages";
		simpMessageTemplate.convertAndSend(urlSubscribe, new ChatMessage(chatmessage.getChatmessage(), chatmessage.getFromname(), chatmessage.getSendTime(), chatmessage.getFromid())); 
	}
	
	@RequestMapping("/chatread/ajax")
	public void ajaxchatmessageRead(@RequestBody String json) throws IOException {
		chatService.updateChatRead(json);
	}

	
	@RequestMapping(value="/chatList", method=RequestMethod.GET)
	public String getChatList(Model model, HttpSession session) {

		 return "chat/chatList";
	}
	
	@RequestMapping(value="/chatUnreadAlert/ajax", method=RequestMethod.POST)
	@ResponseBody
	public int chatUnread(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
    	int messages = chatService.getUnreadMessages(email);

		return messages;
	}

	@RequestMapping(value="/chatUnreadMessageInfo/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatListUnread(@RequestBody String json) {
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
		List<ChatList> chatmessageList = chatService.getAllChatRoom(email);
		JSONArray ja = new JSONArray();
		List<Integer> unreadChatId = chatService.getUnreadMessages(email);

		 for (ChatList chatList : chatmessageList) {
			//chatmessage 정보를 JSON Object에 put 해줌, chatmessage이 반복문에서 넘어갈 때마다 객체 초기화 
			 JSONObject jo = new JSONObject();
			 jo.put("pr_id", chatList.getPr_id());
			 jo.put("buyerId", chatList.getBuyerId());
			 jo.put("pr_img_1", chatList.getPr_img_1());
		 	//리스트에 출력할 상대방 닉네임 확인
		 if (chatList.getBuyerId().equals(email)) {
			 jo.put("senderName", chatList.getSellerName());
		 } else {
			 jo.put("senderName", chatList.getBuyerName());
		 }
		 
		 	 jo.put("pr_title", chatList.getPr_title());
		 //읽지 않은 chatmessage이 0개일때
		 if (unreadChatId.size() == 0) {
			 jo.put("messageUnread", "");
		 	} else {
		 		//읽지 않은 chatmessageId들과 현재 chatmessageId 대조 후 처리 
				 for (int ele : unreadChatId) {
					 	if (chatList.getId() == ele) {
					 		jo.put("messageUnread", "새 메세지");
					 		break;
					 	} else {
					 		jo.put("messageUnread", "");
					 	}
				 }
			}
		 	ja.put(jo);
		}
		 //Javascript에 parsing 할 수 있도록 JSON Object에 Array를 담아줌
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", ja);
		 //String으로 변환해주면 끝, 프런트<->백엔드 전달은 String으로 이루어지며 형식은 JSON을 선택했음 
		 String result = jsnResult.toString();
		 //View로 result를 return해줌
		 return result;
	}
}
