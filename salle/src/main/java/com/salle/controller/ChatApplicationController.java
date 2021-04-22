package com.salle.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.salle.domain.ChatMessage;
import com.salle.domain.ChatRoom;

@Controller
public class ChatApplicationController {
	Logger log = LoggerFactory.getLogger(ChatApplicationController.class);
	
	private SimpMessagingTemplate simpMessageTemplate;
	private ChatService chatService;
	
	@Autowired
	public ChatApplicationController(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService) {
		this.simpMessageTemplate = simpMessagingTemplate;
		this.chatService = chatService;
	}
	
	@RequestMapping(value="/product/chatStart/{pr_id}", method=RequestMethod.GET)
	public String productChatMessage(Model model, HttpSession session, 
			@PathVariable("pr_id") String pr_idStr) throws IOException {
		
		if (session.getAttribute("login") == null) {
			return "/login";
		}
		
		int pr_id = Integer.parseInt(pr_idStr);
		Map<String, String> chatInfoMap = new HashMap<String, String>();
		chatInfoMap = chatService.productInfoSetting(session, pr_id, chatInfoMap);
		List<ChatMessage> chatHistory = chatService.readChatHistory(chatInfoMap.get("chatid"));
		if (chatHistory != null)
			model.addAttribute("chatHistory", chatHistory);
		model.mergeAttributes(chatInfoMap);
		return "chat/chatmessage";
	}
	
	@RequestMapping(value="/chatList/chatStart/{chatid}", method=RequestMethod.GET)
	public String getchatmessage(Model model, @PathVariable("chatid") String chatid, 
			HttpSession session) throws IOException {
		List<ChatMessage> chatHistory = chatService.readChatHistory(chatid);
		model.addAttribute("chatHistory", chatHistory);
		Map<String, String> chatInfomap = new HashMap<String, String>();
		ChatRoom chatRoom = chatService.getChatRoom(chatid);
		chatInfomap = chatService.chatInfoSetting(chatInfomap, session, chatRoom);
		model.mergeAttributes(chatInfomap);
		return "chat/chatmessage";
	}
	
	@MessageMapping("/chat")
	public void send(ChatMessage chatMessage) throws IOException {
		ChatMessage chatMessageInfo = chatService.appendMessage(chatMessage);
		String urlSubscribe = "/subscribe/" + chatMessageInfo.getChatid();
		simpMessageTemplate.convertAndSend(urlSubscribe, new ChatMessage(chatMessageInfo.getMessage(), chatMessageInfo.getFromname(),
				chatMessageInfo.getSendtime(), chatMessageInfo.getFromid())); 
	}

	@RequestMapping(value="/chatList", method=RequestMethod.GET)
	public ModelAndView getChatList(HttpSession session) {
		ModelAndView mav = new ModelAndView("chat/chatList");
		return mav;
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
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
		List<ChatRoom> chatRoomList = chatService.getAllChatRoom(email);
		String result = chatService.chatMessageJson(chatRoomList, email);
		return result;
	}
	
	@RequestMapping("/chatread/ajax")
	public void ajaxchatmessageRead(@RequestBody String json) throws IOException {
		chatService.updateChatRead(json);
	}
	
	@RequestMapping(value="/chatUnreadAlert/ajax", method=RequestMethod.POST)
	@ResponseBody
	public int chatUnread(@RequestBody String json) {
		JSONObject jsn = new JSONObject(json);
		String email = (String) jsn.get("email");
    	int messages = chatService.getUnreadMessages(email);
		return messages;
	}
}
