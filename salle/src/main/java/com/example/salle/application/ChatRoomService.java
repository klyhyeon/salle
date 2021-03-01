package com.example.salle.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.salle.domain.ChatList;
import com.example.salle.domain.ChatRoom;
import com.example.salle.mapper.ChatRoomMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService implements ChatRoomMapper {
	
	
	private final AmazonS3Client amazonS3Client;
	
	@Autowired
	ChatRoomMapper chatRoomMapper;
	
	//application.properties에 설정
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

	@Override
	public void addChatRoom(ChatRoom chatRoom) throws IOException {
		
		Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());
        
        chatRoom.setCreatedDate(createdDate);
		
		chatRoomMapper.addChatRoom(chatRoom);
		
	}
	
	//StringBuilder or BufferedReader or BufferedWriter 사용해서 불러오기
	public List<ChatRoom> readChatHistory(ChatRoom chatRoom) throws IOException {
		//View에 ChatRoom 객체로 전달
		ChatRoom chatRoomLines = new ChatRoom();
		List<ChatRoom> chatHistoryList = new ArrayList<ChatRoom>();
		int id = chatRoom.getId();
		List<String> chatHistoryMessage = chatRoomMapper.getAllChatMessages(id);
		
		for (String historyMessage : chatHistoryMessage) {
			BufferedReader br = new BufferedReader(new StringReader(historyMessage));
			String chatLine;
			int idx = 1;
			
			while ((chatLine = br.readLine()) != null) {
				
				//1개 메시지는 3줄(보낸사람,메시지내용,보낸시간)로 구성돼있음
				int answer = idx % 3;
				if (answer == 1) {
					//보낸사람
					chatRoomLines.setSenderName(chatLine);
					idx++;
				} else if (answer == 2) {
					//메시지내용
					chatRoomLines.setContent(chatLine);
					idx++;
				} else {
					//보낸시간
					chatRoomLines.setSendTime(chatLine);
					//메시지 담긴 ChatRoom 객체 List에 저장
					chatHistoryList.add(chatRoomLines);
					//객체 초기화, 줄(row)인덱스 초기화
					chatRoomLines = new ChatRoom();
					idx = 1;
				}			
			}
		}

		
		return chatHistoryList;
	}
	
	@Override
	public void updateFileName(int id, String fileName) {

		chatRoomMapper.updateFileName(id, fileName);
	}
	
	@Override
	public List<ChatList> findByEmail(String email) {
		
		return chatRoomMapper.findByEmail(email);
	}

	@Override
	public int countByChatId(int pr_id, String buyerId) {
		
		return chatRoomMapper.countByChatId(pr_id, buyerId);
	}


	//no connection with DB
	public void appendMessage(ChatRoom chatRoom) throws IOException {
		
		//TODO: S3에서 파일 받아오기, 추가 후 업로드까지
			//**사용자 채팅칠 때마다 S3object를 받아서 로컬파일로 만들고 채팅내용을 append해서 S3로 put해줘야한다?
			//	과부하가 발생할 것임
		int pr_id = chatRoom.getPr_id();
		int id = chatRoom.getId();
		String buyerId = chatRoom.getBuyerId();
		String content = chatRoom.getContent();
		String senderName = chatRoom.getSenderName();
		String senderId = chatRoom.getSenderId();
		String sendTime = chatRoom.getSendTime();
		System.out.println("print:" + content);
		
		String chatMessage = senderName + "\n" + content + "\n" + "[" +  sendTime + "]" + "\n";
		chatRoomMapper.insertChatMessage(chatMessage, id);
		
		System.out.println("senderId: "+ senderId);
		System.out.println("sellerId: "+ chatRoom.getSellerId());
		System.out.println(senderId.equals(chatRoom.getSellerId()));
		if (senderId.equals(chatRoom.getSellerId())) {
			updateChatReadBuy(chatRoom.getId(), 0);
		} else {
			updateChatReadSell(chatRoom.getId(), 0);
		}
		
	}

	@Override
	public ChatRoom findByChatId(int pr_id, String buyerId) {
		return chatRoomMapper.findByChatId(pr_id, buyerId);
	}
	
	@Override
	public int getId(int pr_id, String buyerId) {
		
		return chatRoomMapper.getId(pr_id, buyerId);
	}

	@Override
	public void updateChatReadBuy(int id, int chatReadBuy) {
		
		chatRoomMapper.updateChatReadBuy(id, chatReadBuy);
		
	}

	@Override
	public void updateChatReadSell(int id, int chatReadSell) {
		
		chatRoomMapper.updateChatReadSell(id, chatReadSell);
		
	}
	
	@Override
	public int getUnreadMessages(String email) {
		
		return chatRoomMapper.getUnreadMessages(email);
	}

	@Override
	public List<Integer> getUnreadChatRoom(String email) {
		
		List<Integer> unread = chatRoomMapper.getUnreadChatRoom(email); 
		return unread;
	}

	@Override
	public void insertChatMessage(String chatMessage, int id) {
		chatRoomMapper.insertChatMessage(chatMessage, id);
	}

	@Override
	public List<String> getAllChatMessages(int id) {
		return chatRoomMapper.getAllChatMessages(id);
	}


	

	
	

}
