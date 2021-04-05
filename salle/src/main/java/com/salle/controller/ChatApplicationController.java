package com.salle.controller;

import java.io.IOException;
import java.util.ArrayList;
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
			@PathVariable("pr_id") String pr_idStr) throws IOException {
		int pr_id = Integer.parseInt(pr_idStr);
		ChatMessage chatMessage = new ChatMessage(pr_id);
		List<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
		ChatMessage chatMessageInfo = chatService.infoSetting(session, chatMessage, 
											chatHistory);
		String pr_title = productService.getProductInfo(pr_id).getPr_title();
		model.addAttribute("chatMessageInfo", chatMessageInfo);
		model.addAttribute("pr_title", pr_title);
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
	
	@MessageMapping("send")
	public void send(ChatMessage chatMessage) throws IOException {
		ChatMessage chatMessageAppen = chatService.appendMessage(chatMessage);
		String chatid = chatMessageAppen.getChatid();
		String urlSubscribe = "/subscribe/" + chatid;
		simpMessageTemplate.convertAndSend(urlSubscribe, new ChatMessage(chatMessageAppen.getChatmessage(), chatMessageAppen.getFromname(),
				chatMessageAppen.getSendtime(), chatMessageAppen.getFromid())); 
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
