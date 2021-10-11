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

<%-- <%@include file="home.jsp" %>--%>
	
	<c:forEach var="chatMessage" items="${chatRoomList}" varStatus="loop">
			<div class="chatMessageInfo">
				<a href="/chatRoom/${chatMessage.pr_id}/${chatMessage.buyerId}">						
					<div class="wrapPr_img">
						<img class="pr_img" src="${chatMessage.pr_img_1}">
					</div>
					<div class="wrapSellerTitle">
						<h3>
							<span id="sellerName">${chatMessage.senderName}&nbsp;</span>
							<span id="title">${chatMessage.pr_title}</span>		
							<span id="message">${chatMessage.chatRoomRead}</span>
							<span class="message${loop.index}"></span>
							<input type="hidden" id="message${loop.index}" value="${chatMessage.chatRoomRead}"/>
						</h3>
					</div>
				</a>
			</div>
	 </c:forEach>
	 
	 <script type="text/javascript">
	 
	 //home.jsp에 있음
	 	var email = document.getElementById('emailInput').value;
		 
		 $(document).ready(function unreadAlertInfinite())
		 
		 
		 	function getUnread() {
			 
			 $.ajax({
				 url:"/chatList/ajax",
				 type: "POST",
				 data: JSON.stringify({
					 email: email
				 }),
				 dataType: "json",
				 contentType: "application/json",
				 success: function(result) {
					 
				 }
			 });
			 
		 }
	 
	 	function unreadAlertInfinite() {
	 		
		 	getUnread();
	 	}
	 	
	 </script>
</body>
</html>