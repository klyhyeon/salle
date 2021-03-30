package com.salle.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.salle.application.ChatService;
import com.salle.application.ProductService;
import com.salle.domain.ChatMessage;
import com.salle.domain.ChatRoom;

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

	@RequestMapping(value="/product/chatStart/{pr_id}", method=RequestMethod.GET)
	public String productChatMessage(Model model, HttpSession session, 
			@PathVariable int pr_id) throws IOException {
		ChatMessage chatMessage = new ChatMessage(pr_id);
		List<ChatMessage> chatHistory = null;
		ChatMessage chatMessageInfo = chatService.infoSetting(session, chatMessage, 
											chatHistory);
		model.addAttribute("chatMessageInfo", chatMessageInfo);
		if (chatHistory != null) 
			model.addAttribute("chatHistory", chatHistory);
		return "chat/chatmessage";
	}
	
	@RequestMapping(value="/chatList/chatStart/{chat_id}", method=RequestMethod.GET)
	public String getchatmessage(Model model, @PathVariable String chatid, HttpSession session) throws IOException {
		
		List<ChatMessage> chatHistory = chatService.readChatHistory(chatid);
		model.addAttribute("chatHistory", chatHistory);
		
		ChatMessage chatMessageInfo = chatService.getChatMessageInfo(chatid);
		chatMessageInfo = chatService.insertToInfo(chatMessageInfo, session);
		model.addAttribute("chatMessageInfo", chatMessageInfo);
		return "chat/chatmessage";
	}
	
	@MessageMapping("/broadcast")
	public void send(ChatMessage chatMessage) throws IOException {
		
		ChatMessage chatMessageAppen = chatService.appendMessage(chatMessage);
		String chatid = chatMessageAppen.getChatid();
		String urlSubscribe = "/user/" + chatid + "/queue/messages";
		simpMessageTemplate.convertAndSend(urlSubscribe, new ChatMessage(chatMessageAppen.getChatmessage(), chatMessageAppen.getFromname(),
				chatMessageAppen.getSendtime(), chatMessageAppen.getFromid())); 
	}
	
	@RequestMapping("/chatread/ajax")
	public void ajaxchatmessageRead(@RequestBody String json) throws IOException {
		chatService.updateChatRead(json);
	}

	
	@RequestMapping(value="/chatList", method=RequestMethod.GET)
	public ModelAndView getChatList(HttpSession session) {
		ModelAndView mav = new ModelAndView("chat/chatList");
		return mav;
	}
	
	@RequestMapping(value="/chatUnreadAlert/ajax", method=RequestMethod.POST)
	@ResponseBody
	public int chatUnread(@RequestBody String json) {
		
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
    	int messages = chatService.getUnreadMessages(email);
		return messages;
	}
	
	@RequestMapping(value="/chatInfo/static/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatList(@RequestBody String json) {
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
		List<ChatRoom> chatRoomList = chatService.getAllChatRoom(email);
		String result = chatService.chatRoomJsonArr(chatRoomList, email);
		return result;
	}

	@RequestMapping(value="/chatInfo/update/ajax", method=RequestMethod.POST)
	@ResponseBody
	public String chatListUnread(@RequestBody String json) {
		//TODO: Add chatMessage
		String headChatMessage = getHeadChatMessage(chatRoom.getChatid());
		jsnObj.put("chatMessage", headChatMessage);
		
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
		List<ChatRoom> chatmessageList = chatService.getAllChatRoom(email);
		JSONArray ja = new JSONArray();
		List<Integer> unreadChatId = chatService.getUnreadMessages(email);

		 for (ChatRoom chatList : chatmessageList) {
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
		 Javascript에 parsing 할 수 있도록 JSON Object에 Array를 담아줌
		 JSONObject jsnResult = new JSONObject();
		 jsnResult.put("chatList", ja);
		 //String으로 변환해주면 끝, 프런트<->백엔드 전달은 String으로 이루어지며 형식은 JSON을 선택했음 
		 String result = jsnResult.toString();
		 //View로 result를 return해줌
		 return result;
	}
}
