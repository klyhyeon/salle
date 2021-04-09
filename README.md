## 중고거래 웹서비스 salle
> 채팅 기반의 중고거래 플랫폼

- Spring MVC 패턴과 REST API로 웹서비스 제작 및 AWS 서버 배포 진행
___
## 기술스택
- Backend: Spring boot | Java | MariaDB(AWS RDS) | AWS S3 | Mybatis
- Frontend: JavaScript(jQuery)
- Server: AWS EC2 | Ubuntu
___
## 참여
- 백호
___
## 기능구현 설명
___
### 회원관리(Member)
- 구조 : modelAttribute로 전달받은 Member객체 DB에 저장
- 비밀번호 암호화 : jasypt encryption
`jasypt encryption`
```
    @Override
    public void insertMember(Member member) {
    	ConfigurablePasswordEncryptor encryptor = new ConfigurablePasswordEncryptor(); 
    	encryptor.setAlgorithm("MD5");
    	encryptor.setPlainDigest(true);
    	String rawPwd = member.getPassword();
    	String encryptedPwd = encryptor.encryptPassword(rawPwd);
    	member.setPassword(encryptedPwd);
        memberMapper.insertMember(member);
    }
```
___
### 상품등록(Product)
- 구조 : modelAttribute로 전달받은 Member객체 DB에 저장
- 한 버튼으로 이미지 여러개 저장 : Javascript FileReader, FormData 사용해 ajax로 비동기전송
```
 var img_count = 1;
 var formData = new FormData();

	$("#img").change(function fileadd() {
		var reader = new FileReader;
		var showImg = "<div class='pr_img_"+ (img_count)+"'><img id='img_"+(img_count)+"' src=''/><button type='button' class='button_img'    value='pr_img_"+(img_count)+"' onclick='deleteImg(this.value)'></button></div>";
		var img_count_string = img_count.toString();
		$(".wrap_pr_img").append(showImg);

		reader.onload = function(data) {
			$('#img_' + img_count_string).attr('src', data.target.result).width(150).height(150);
		};
		reader.readAsDataURL(this.files[0]);
		formData.append('pr_img_' + img_count_string, this.files[0]);
  	img_count++;		
  });
    
	function fileUpload() {

		$.ajax({
    		url:"/productReg/ajax",
   			type: 'POST',
    		data: formData,
    			processData: false,
    			contentType: false,
    			success: function(data) {
    				console.log('jQuery ajax form submit success');
    				}
    		}); //end ajax
    		
    	formData.delete;
	}
```
___
### 채팅(Chat)
- 구조 : 
	- STOMP를 이용해 View에서 전달해준 message payload를 Controller에서 처리 후 SimpMessagingTemplate API로 View에 전송
	- 네트워크 이상, DB오류 발생 시 fallback 받을 수 있도록 SockJS 사용
	- DB 테이블 : 채팅메시지(ChatMessage), 채팅방(ChatRoom) 이원화 관리

`WebSocket Config`
```
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/subscribe");
		registry.setApplicationDestinationPrefixes("/send");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/sockJS")
			.withSockJS()
			.setHeartbeatTime(60_000);
	}

}
```
`MessageMapping Controller`
```
@MessageMapping("/chat")
public void send(ChatMessage chatMessage) throws IOException {
	ChatMessage chatMessageInfo = chatService.appendMessage(chatMessage);
	String urlSubscribe = "/subscribe/" + chatMessageInfo.getChatid();
	simpMessageTemplate.convertAndSend(urlSubscribe, new ChatMessage(chatMessageInfo.getMessage(), chatMessageInfo.getFromname(),
			chatMessageInfo.getSendtime(), chatMessageInfo.getFromid())); 
}
```

`chatList View`

<img width="414" alt="210409_salle_chatList" src="https://user-images.githubusercontent.com/61368705/114121427-4c6f3f00-9929-11eb-9d71-a2757463e35c.png">



`chatRoom View(GIF)`

![chatRoom2](https://user-images.githubusercontent.com/61368705/114122927-3f078400-992c-11eb-88f2-d845cabe8183.gif)



`chat DB Table`

<img width="352" alt="210409_salle_chatDB" src="https://user-images.githubusercontent.com/61368705/114120413-67d94a80-9927-11eb-972a-e2ed65141907.png">


