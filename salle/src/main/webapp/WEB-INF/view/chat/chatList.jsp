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

<%@include file="../home.jsp" %>

	 
	 <script type="text/javascript">
	 	var email = document.getElementById('emailInput').value;
		$(document).ready(initialize);
		
		function initialize() {
			getChatInfoStatic();
			getChatInfoUpdate();k
			unreadAlertInfinite();
		}
		 
		function getChatInfoStatic() {
			$.ajax({
				url: "/chatInfo/static/ajax",
				type: "POST",
				data: JSON.stringify({
					email: email
				}),
				contentType: "application/json",
				success: function(data) {
					 var parsed = JSON.parse(data);
					 var length = parsed.chatList.length;
					 for (var idx = 0; idx < length; idx++) {
						 staticInfo(idx, parsed.chatList[idx].pr_img_1,
								 parsed.chatList[idx].username, parsed.chatList[idx].chatid);
					 }
				}
			});
		}
		
		function getChatInfoUpdate() {
			$.ajax({
				url: "/chatInfo/update/ajax",
				type: "POST",
				data: JSON.stringify({
					email: email
				}),
				contentType: "application/json",
				success: function(data) {
					var parsed = JSON.parse(data);
					var length = parsed.chatList.length;
					console.log('chatInfo update invoked');
					for (var idx = 0; idx < length; idx++) {
						$('.wrapChatRoomInfo' + idx).html('');
						console.log('message: ' + parsed.chatList[idx].message);
					 	updateInfo(parsed.chatList[idx].message, 0, idx);
					}
				}
		 	});
		}
	 			 
	 	function unreadAlertInfinite() {
	 		setInterval(() => {
	 			getChatInfoUpdate();				
			}, 1000);
	 	}
	 	
	 	function updateInfo(message, messageUnread, idx) {
	 		var str =
	 			'<span id="message">' +
	 			message +
	 			'&nbsp' +
	 			messageUnread + 
	 			'</span>';
	 		$('.wrapChatRoomInfo' + idx).append(str);
	 	} 
	 	
	 	//페이지가 로드되는 시점 한 번만 출력하면 되는 div, img를 출력하는 메서드
	 	function staticInfo(idx, pr_img_1, username, chatid) {
	 			$(document.body).append('<div class= chatMessageInfo' + 
	 					idx + 
	 					'><a href="/chatList/chatStart/' + 
	 					chatid + 
	 					'"><div class="wrapPr_img"><img class="pr_img" src="' + 
	 					'${s3Url}' + 
	 					pr_img_1 +
	 					'"></div><div class="username">' + 
	 					username + 
	 					'</div><div class="wrapChatRoomInfo' +
	 					idx +
	 					'"></div></a></div>');
	 	}
	 </script>
</body>
</html>