package com.example.salle.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.salle.application.ChatService;
import com.example.salle.domain.ChatList;
import com.example.salle.domain.Chatmessage;
import com.example.salle.domain.Login;

@Controller
public class ChatApplicationController {
	
	private SimpMessagingTemplate simpMessageTemplate;
	private ChatService chatService;
	
	@Autowired
	public ChatApplicationController(SimpMessagingTemplate simpMessagingTemplate, 
			ChatService chatService) {
		this.simpMessageTemplate = simpMessagingTemplate;
		this.chatService = chatService;
	}

	//채팅으로 거래하기(productInfo 화면)
	@RequestMapping(value="/product/chatStart", method=RequestMethod.GET)
	public String productChatMessage(Model model, HttpSession session, 
			@ModelAttribute("chatmessage") Chatmessage chatmessage) throws IOException {
		Chatmessage chatmessageInfoAdded = chatService.infoSetting(session, chatmessage);
		int pr_id = chatmessage.getPr_id();
		Chatmessage chatmessageExist = chatService.findByChatId(pr_id, chatmessageInfoAdded.getFromid());
		if (chatmessageExist != null) {
			List<Chatmessage> chatHistory = chatService.readChatHistory(pr_id, chatmessageInfoAdded.getFromid());
			model.addAttribute("chatHistory", chatHistory);
		}
			model.addAttribute("chatmessageInfo", chatmessageInfoAdded);
		return "chat/chatmessage";
	}
	
	@RequestMapping(value="/chatList/chatStart", method=RequestMethod.GET)
	public String getchatmessage(@PathVariable Map<String, String> requestVar,
			Model model) throws IOException {
		
		String buyerId = requestVar.get("buyerId");
		int pr_id = Integer.parseInt(requestVar.get("pr_id"));
		String chatid = pr_id + buyerId;
			
		//read chatHistory
		Chatmessage chatmessageRead = chatService.findByChatId(pr_id, buyerId);
		List<Chatmessage> chatHistory = chatService.readChatHistory(chatid);
		model.addAttribute("chatHistory", chatHistory);
		
		int id = chatService.getId(pr_id, buyerId);
		String pr_title = chatmessageRead.getPr_title();
		String sellerId = chatmessageRead.getSellerId();
		
		model.addAttribute("id", id);
		model.addAttribute("pr_id", pr_id);
		model.addAttribute("buyerId", buyerId);
		model.addAttribute("sellerId", sellerId);
		model.addAttribute("pr_title", pr_title);
		
		return "chat/chatmessage";
	}
	
	@MessageMapping("/broadcast")
	public void send(Chatmessage chatmessage) throws IOException {
		chatService.appendMessage(chatmessage);
		int id = chatmessage.getId();
		String urlSubscribe = "/user/" + id + "/queue/messages";
		simpMessageTemplate.convertAndSend(urlSubscribe, new Chatmessage(chatmessage.getChatmessage(), chatmessage.getFromname(), chatmessage.getSendtime(), chatmessage.getFromid())); 
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
	
	
	@RequestMapping(value="/chatList/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatList(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
		List<ChatList> chatmessageList = chatService.findByEmail(email);		 
		JSONArray ja = new JSONArray();

		 for (ChatList chatList : chatmessageList) {
			
			 JSONObject jo = new JSONObject();
			 jo.put("pr_img_1", chatList.getPr_img_1());
			 ja.put(jo);
		}
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", ja);
		 String result = jsnResult.toString();
		 System.out.println("chatResult toString: " + result);
		
		 return result;
	}
	
	
}
