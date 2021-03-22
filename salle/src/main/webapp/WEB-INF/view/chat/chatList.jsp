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
			getChatList();
			getUnreadMessageInfo();
			unreadAlertInfinite();
		}
		 
		//async(비동기)로 일정간격 업뎃되지 않아도 되는 img 파일을 불러옴
		function getChatList() {
			console.log("getChatList inprocess");
			$.ajax({
				url: "/getChatList/ajax",
				type: "POST",
				data: JSON.stringify({
					email: email
				}),
				contentType: "application/json",
				//전달을 성공했을때 Controller로부터 data를 return 받아 처리해주는 메서드	
				success: function(data) {
					
					 var parsed = JSON.parse(data);
					 var length = parsed.chatList.length;
					 for (var i = 0; i < length; i++) {
						 //채팅방 갯수만큼 반복문을 돌면서 채팅방 틀(div, img 태그)를 만들어줌 
						 addChatDivImg(i, parsed.chatList[i].pr_img_1);
					 }
				}
			});
		}
		
		 //async(비동기) 방식으로 일정간격 업데이트 되어야 하는 정보들(메세지 알림기능) 
		 function getUnreadMessageInfo() {

				 $.ajax({
					 url:"/chatUnreadMessageInfo/ajax",
					 type: "POST",
					 data: JSON.stringify({
						 email: email
					 }),
					 contentType: "application/json",
					 success: function(data) {
						 var parsed = JSON.parse(data);
						 var length = parsed.chatList.length;
						 
						 for (var i = 0; i < length; i++) {
							$('.wrapSellerTitle' + i).html('');
						 	addChatList(parsed.chatList[i].pr_id, parsed.chatList[i].buyerid, parsed.chatList[i].fromname, parsed.chatList[i].pr_title, parsed.chatList[i].messageUnread, i);
						 }
					 }
			 });
		 }
	 	
		 var pr_id = pr_id;
		 var buyerid = buyerid;
		 var fromname = fromname;
		 var pr_title = pr_title;
		 var messageUnread = messageUnread;
		 var chatid = pr_id + buyerid;
		 //1000milliseconds(==1초) 간격으로 getUnreadMessageInfo()를 실행시키는 반복 메서드
	 	function unreadAlertInfinite() {
	 		setInterval(() => {
	 			getUnreadMessageInfo();				
			}, 100000);
	 	}
	 	
	 	//일정 간격으로 업데이트된 데이터를 화면에 출력하는 메서드 됨
	 	function addChatList(pr_id, buyerid, senderName, pr_title, messageUnread, idx) {

	 		var str =
	 		'<a href="" onclick="getChatMessage()">' +
	 		'<h3><span id="sellerName">' + 
	 		senderName +
	 		'&nbsp;</span>' +
	 		'<span id="title">' + 
	 		pr_title + 
	 		'</span><span id="message">' + 
	 		messageUnread+'</span></h3></div></a>';
	 		
	 		//HTML화면의 <div class="wrapSellerTitle0,1,...etc"> 하위에 str 변수를 추가해준다.		 		 
	 		 $('.wrapSellerTitle' + idx).append(str);
	 	} 
	 	
	 	//페이지가 로드되는 시점 한 번만 출력하면 되는 div, img를 출력하는 메서드
	 	function addChatDivImg(idx, img) {
	 			$(document.body).append('<div class= chatMessageInfo' + idx + '><div class="wrapPr_img"><img class="pr_img" src="${s3Url}' + img + '"></div><div class="wrapSellerTitle' +
	 					idx +
	 					'"></div></div>');
	 	}
	 	
	 	function getChatMessage() {
	 		var xttp = new XMLHttpRequest();
	 		xttp.open("GET", "/chatList/chatStart", false);
	 		xttp.onload = function() {
	 			console.log("status" + xttp.status);
	 		}
	 		xttp.send(chatid);
	 	}
	 	

	 	
	 </script>
</body>
</html>