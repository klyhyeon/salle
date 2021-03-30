<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ChatList</title>
<link rel="stylesheet" href="/resources/css/chatList.css">
</head>
<body>

<%@include file="home.jsp" %>

	<div class="wrapper">
	
	</div>
	 
	 <script type="text/javascript">
	 	var email = document.getElementById('emailInput').value;
		$(document).ready(initialize());
		
		function initialize() {
			getChatInfoStatic();
			getChatInfoUpdate();
			unreadAlertInfinite();
		}
		 
		function getChatInfoStatic() {
			console.log("getChatList inprocess");
			$.ajax({
				url: "/chatInfo/static/ajax",
				type: "POST",
				data: JSON.stringify({
					email: email
				}),
				contentType: "application/json",
				//전달을 성공했을때 Controller로부터 data를 return 받아 처리해주는 메서드	
				success: function(data) {
					
					 var parsed = JSON.parse(data);
					 var length = parsed.chatList.length;
					 for (var idx = 0; idx < length; i++) {
						 //채팅방 갯수만큼 반복문을 돌면서 채팅방 틀(div, img 태그)를 만들어줌 
						 addChatDivImg(idx, parsed.chatList[idx].pr_img_1, parsed.chatList[idx].pr_title, 
								 parsed.chatList[idx].username, parsed.chatList[idx].chatid);
					 }
				}
			});
		}
		
		function getChatInfoUpdate() {
			 $.ajax({
				 url:"/chatInfo/update/ajax",
				 type: "POST",
				 data: JSON.stringify({
					 email: email
				 }),
				 contentType: "application/json",
				 success: function(data) {
					 var parsed = JSON.parse(data);
					 var length = parsed.chatList.length;
					 
					 for (var i = 0; i < length; i++) {
						$('.wrapChatRoomInfo' + i).html('');
					 	addChatList(parsed.chatList[i].chatMessage, 0, i);
					 }
				 }
		 });
		}
	 			 
	 	function unreadAlertInfinite() {
	 		setInterval(() => {
	 			getUnreadMessageInfo();				
			}, 1000);
	 	}
	 	
	 	//일정 간격으로 업데이트된 데이터를 화면에 출력하는 메서드 됨
	 	function addChatList(username, pr_title, chatid, 
	 			messageUnread, idx) {

	 		var str =
	 			'<a href="/chatList/chatStart/' + chatid + '">' +
	 			'<h3><span id="username">' + 
	 			username +
	 			'&nbsp;</span>' +
	 			'<span id="title">' + 
	 			pr_title + 
	 			'</span><span id="message">' + 
	 			messageUnread+'</span></h3></div></a>';
	 		
	 		//HTML화면의 <div class="wrapSellerTitle0,1,...etc"> 하위에 str 변수를 추가해준다.		 		 
	 		$('.wrapChatRoomInfo' + idx).append(str);
	 	} 
	 	
	 	//페이지가 로드되는 시점 한 번만 출력하면 되는 div, img를 출력하는 메서드
	 	function addChatDivImg(idx, pr_img_1, pr_title, username, chatid) {
	 			$(document.body).append('<div class= chatMessageInfo' + 
	 					idx + 
	 					'><a href="/chatList/chatStart/' + 
	 					chatid + 
	 					'"><div class="wrapPr_img"><img class="pr_img" src="' + 
	 					${s3Url} + pr_img_1 + 
	 					'"></div><div class="wrapChatRoomInfo' +
	 					idx +
	 					'"></div><span id="username">' + 
	 					username + 
	 					'</span></a></div>');
	 	}
	 </script>
</body>
</html>