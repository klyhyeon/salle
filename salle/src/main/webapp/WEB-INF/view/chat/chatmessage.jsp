<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${pr_title}</title>
	
	<link rel="stylesheet" href="/resources/css/chatBroadcastProduct.css">

</head>
<body>
	<div class="container">
		<div class="title_text">
			<h2>${pr_title}</h2>
		</div>
		<div class="row">	
				<div id="content">
					<c:forEach var="chatRoom" items="${chatHistory}">
						<p>
							<span id="chatRoomSenderName">${chatMessage.fromname}</span><br>
							<span id="chatRoomContent">${chatMessage.chatmessage}</span><br>
							<span id="chatRoomSendTime">${chatMessage.sendTime}</span><br>
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
							<input type="hidden" value="${chatmessageInfo.pr_id}" id="pr_id"/>
							<input type="hidden" value="${chatmessageInfo.toid}" id="toid"/>
							<input type="hidden" value="${chatmessageInfo.toname}" id="toname"/>						
							<input type="hidden" value="${chatmessageInfo.pr_email}" id="pr_email"/>				
						</div>					
					</div>				
				</div>
			</div>
	</div>
	
	<script src="/webjars/stomp-websocket/2.3.3-1/stomp.js" type="text/javascript"></script>
	<script src="/webjars/sockjs-client/1.1.2/sockjs.js" type="text/javascript"></script>
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
	<script type="text/javascript">
		var stompClient = null;
		var fromname = $('#fromname').val();
		var fromid = $('#fromid').val();
		var pr_id = $('#pr_id').val();
		var toname = $('#toname').val();
		var toid = $('#toid').val();	
		var pr_email = $("#pr_email").val();
		var buyerid = "";
		var chatid = "";
		
		$(document).ready(function test() {
			console.log('test');
		});
		//$(document).ready(ajaxChatRead());
		
		function connect() {
			console.log("connected");
			getBuyerid(pr_email, fromid, toid);
			var urlSubscribe = '/user/' + chatid + '/queue/messages';
			stompClient = Stomp.over(function(){
				return new SockJS('/broadcast');	
			});
		}
			
		stompClient.connect({}, function() {
				stompClient.subscribe(urlSubscribe, function(output) {
					showBroadcastMessage(createTextNode(JSON.parse(output.body)));
				});
			}, 
			function (err) {
						alert('error' + err);
			});
		
		
		function getBuyerid(String pr_email, String fromid, String toid) {
			if (pr_email === fromid) {
				buyerid = toid;
			} else {
				buyerid = fromid;
			}
			chatid = pr_id + buyerid;
		}
		
		function sendBroadcast(json) {
			stompClient.send("/app/broadcast", {}, JSON.stringify(json));
		}
		
		function send() {
			var content = $('#message').val();
			console.log(content);
			sendBroadcast({
				'pr_id': pr_id, 
				'fromname': fromname,
				'toname': toname,
				'fromid': fromid, 
				'toid': toid, 
				'chatmessage': content,
				'chatid': chatid
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
			console.log("createTextNode");
			console.log("messageObj: " + messageObj.content);
            return '<p><div class="row alert alert-info"><div class="col_8">' +
            messageObj.senderName +
            '</div><div class="col_4 text-right">' +
            messageObj.content+
            '</div><div>[' +
            messageObj.sendTime +
            ']</div></p>';
        }
		
		function showBroadcastMessage(message) {
            $("#content").html($("#content").html() + message);
        }
		

		<%-- 읽음처리 
		function ajaxChatRead(id, reader) {
			console.log("ajaxChatread");
			var flag = "";
			if (reader == buyerId) {
				flag = "buy";
			} else {
				flag = "sell";
			}
			$.ajax({
				url:'/chatread/ajax',
				type: 'POST',
				data: JSON.stringify({
					pr_id: pr_id,
					fromid: toid,
					toid: fromid
				}),
				dataType: 'json',
				contentType: 'application/json'
			})
		}
		--%>
	
	</script>
</body>
</html>