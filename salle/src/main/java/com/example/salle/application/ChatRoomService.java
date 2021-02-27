package com.example.salle.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
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
	
	//no connection with DB
	public List<ChatRoom> readChatHistory(ChatRoom chatRoom) throws IOException {
		
		//TODO: S3에서 해당파일 받아오기
		String fileName = chatRoom.getFileName();
		S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileName));
		BufferedReader br = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));

		//View에 ChatRoom 객체로 전달
		ChatRoom chatRoomLines = new ChatRoom();
		List<ChatRoom> chatHistory = new ArrayList<ChatRoom>();

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
				chatHistory.add(chatRoomLines);
				//객체 초기화, 줄(row)인덱스 초기화
				chatRoomLines = new ChatRoom();
				idx = 1;
			}			
		}
		
		return chatHistory;
	}
	
	
	@Override
	public void updateFileName(int id, String fileName) {

		chatRoomMapper.updateFileName(id, fileName);
	}
	
	public void createFile(int pr_id, int id) throws IOException {
		
		String dirName = "/static/img";
		String fileName = pr_id + "_" + id + ".txt";
		String pathName = bucket + dirName + fileName;
		//File 클래스에 pathName 할당
		File txtFile = new File(pathName);
		String uploadTxtUrl = putS3(txtFile, fileName);
		
		chatRoomMapper.updateFileName(id, uploadTxtUrl);
	}
	
	private String putS3(File uploadFile, String fileName) {
		//withCannedAcl, CannedAccessControList.PublicRead: Allow all users access(ACL) permitted 
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
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
		String buyerId = chatRoom.getBuyerId();
		
		ChatRoom chatRoomAppend = chatRoomMapper.findByChatId(pr_id, buyerId);
				
		String fileName = chatRoomAppend.getFileName();
		
		S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileName));
		BufferedReader br = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
		
		FileOutputStream fos = new FileOutputStream(pathName, true);
		String content = chatRoom.getContent();
		String senderName = chatRoom.getSenderName();
		String senderId = chatRoom.getSenderId();
		String sendTime = chatRoom.getSendTime();
		System.out.println("print:" + content);
		
		String writeContent = senderName + "\n" + content + "\n" + "[" +  sendTime + "]" + "\n";
		
		byte[] b = writeContent.getBytes();
		
		fos.write(b);
		fos.close();
		
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


	

	
	

}
