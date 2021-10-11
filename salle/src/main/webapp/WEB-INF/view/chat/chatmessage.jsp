<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${pr_title}</title>
	
	<link rel="stylesheet" href="/css/chatBroadcastProduct.css">

</head>
<body>	
	<div class="container">
		<div class="title_text">
			<h2>${pr_title}</h2>
		</div>
		<div class="row">	
				<div id="content">
					<c:forEach var="chatMessage" items="${chatHistory}">
						<p>
							<span id="chatRoomSenderName">${chatMessage.fromname}</span><br>
							<span id="chatRoomContent">${chatMessage.message}</span><br>
							<span id="chatRoomSendTime">${chatMessage.sendtime}</span><br>
						</p>	
					</c:forEach>
				</div>
				<div class="row_3">
					<div class="input_group" id="sendMessage">
						<input type="text" placeholder="Message" id="message" class="form_control"/>
						<div class="input_group_append">
							<button id="send" class="btn btn-primary" onclick="send()">보내기</button>
							<input type="hidden" value="${login.getNickName()}" id="fromname"/>
							<input type="hidden" value="${login.getEmail()}" id="fromid"/>
							<input type="hidden" value="${pr_id}" id="pr_id"/>
							<input type="hidden" value="${pr_email}" id="pr_email"/>				
							<input type="hidden" value="${toid}" id="toid"/>
							<input type="hidden" value="${toname}" id="toname"/>
							<input type="hidden" value="${chatid}" id="chatid"/>
						</div>					
					</div>				
				</div>
			</div>
	</div>
	
	<script src="/webjars/stomp-websocket/2.3.3-1/stomp.js" type="text/javascript"></script>
	<script src="/webjars/sockjs-client/1.1.2/sockjs.js" type="text/javascript"></script>
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
	<script type="text/javascript">
		$(document).ready(connect);

		var stompClient = null;
		var fromname = $('#fromname').val();
		var fromid = $('#fromid').val();
		var pr_id = $('#pr_id').val();
		var toname = $('#toname').val();
		var toid = $('#toid').val();	
		var pr_email = $("#pr_email").val();
		var chatid = $("#chatid").val();
		
		
		function connect() {
			console.log("connected");
			var sockJS = new SockJS('/sockJS');
			var urlSubscribe = '/subscribe/' + chatid;
			stompClient = Stomp.over(sockJS);
			stompClient.connect({}, function() {
				stompClient.subscribe(urlSubscribe, function(output) {
					showBroadcastMessage(createTextNode(JSON.parse(output.body)	));
				});
			}, 
			function (err) {
						alert('error' + err);
			});
		};
		
		function sendBroadcast(json) {
			stompClient.send('/send/chat', {}, JSON.stringify(json));
		}
		
		
		function getTime() {
			var today = new Date();
			var date = today.getFullYear() + '년 ' + (today.getMonth()+1)+'월 '+today.getDate()+'일';
			var minutes = today.getMinutes() + "";
			if (minutes.length == 1)
				minutes = '0' + minutes;
			var time = today.getHours() + ":" + minutes;
			var datetime = date + ' ' + time;			
			return datetime;
		}
		
		function send() {
			var content = $('#message').val();
			var datetime = getTime();
			sendBroadcast({
				'pr_id': pr_id, 
				'fromname': fromname,
				'toname': toname,
				'fromid': fromid, 
				'toid': toid, 
				'message': content,
				'chatid': chatid,
				'sendtime': datetime
				});
			$('#message').val("");
		}
		
		var inputMessage = document.getElementById('message'); 
		inputMessage.addEventListener('keyup', function enterSend(event) {
			if (event.keyCode === null) {
				event.preventDefault();
			}
			if (event.keyCode === 13) {
				send();
			}
		});
		
		function createTextNode(messageObj) {
            return '<p><div class="row alert alert-info"><div class="col_8">' +
            messageObj.fromname +
            '</div><div class="col_4 text-right">' +
            messageObj.message+
            '</div><div>' +
            messageObj.sendtime +
            '</div></p>';
        }
		
		function showBroadcastMessage(message) {
            $("#content").html($("#content").html() + message);
        }
	
	</script>
</body>
</html>