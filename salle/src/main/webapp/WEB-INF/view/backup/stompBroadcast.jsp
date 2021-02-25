<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Web socket and STOMP Example</title>
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
	<!-- <script src="/resources/js/stompBroadcast.js"></script> -->
</head>
<body>
	<div class="container">
		<div class="title_text">
			<h2>Web Socket and STOMP</h2>
			<p>Sample of basic Websocket broadcast with STOMP</p>
		</div>
		<div class="row">
			<div class="col_6">
				<div class="row_3">
					<div class="input_group">
					Websocket connection:&nbsp;
						<div class="btn_group">
							<button type="button" id="connect" class="btn btn-sm btn-outline-secondary" onclick="connect()">connect</button>
							<button type="button" id="disconnect" class="btn btn-sm btn-outline-secondary" onclick="disconnect()" disabled>disconnect</button>
						</div>
					</div>				
				</div>
				<div class="row_3">
					<div class="input_group" id="sendMessage" style="display: none;">
						<input type="text" placeholder="Message" id="message" class="form_control"/>
						<div class="input_group_append">
							<button id="send" class="btn btn-primary" onclick="send()">Send</button>
							<input type="hidden" value="${login.getNickName()}" id="nickName"/>
						</div>
					</div>				
				</div>
			</div>
			
			<div class="col_6">
				<div id="content"></div>
				<div>
					<span class="float-right">
						<button id="clear" class="btn btn-primary" onclick="clearBroadcast()" style="display: none;">Clear</button>				
					</span>
				</div>
			</div>
		</div>
	</div>
	
	
	<script src="/webjars/stomp-websocket/2.3.3-1/stomp.js" type="text/javascript"></script>
	
	<script type="text/javascript">
	
	var stompClient = null;
	var nickName = $('#nickName').val() + ":";
	
		
		function setConnected(connected) {		
			$('#connect').prop('disabled', connected);
			$('#disconnect').prop('disabled', !connected);
			if (connected) {
				$('#sendMessage').show();
			} else {
				$('#sendMessage').hide();				
			}
		};
		
		function connect() {
			var url = "ws://localhost:8080/broadcast";
			stompClient = Stomp.client(url);
			
			stompClient.connect({}, function() {
				stompClient.subscribe('/topic/messages', function(output) {
					console.log("broadcastMessage working");
					showBroadcastMessage(createTextNode(JSON.parse(output.body)));
				});
						sendConnection('connected to server please');
						setConnected(true);				
				}, 
						function (err) {
							alert('error' + err);
				
			});

		};
		
		function disconnect() {
			
			if(stompClient!= null) {
				sendConnection('disconnected from the server');
				
				stompClient.disconnect(function() {
					console.log('disconnected...');
					setConnected(false);
				});
			}
		}
		
		function sendConnection(message) {
			
			var content = nickName + message;
			sendBroadcast({'chatId': 'server', 'content' : content})
		}
		
		function sendBroadcast(json) {
			
			stompClient.send("/app/broadcast", {}, JSON.stringify(json));
		}
		
		function send() {
			var content = $('#message').val();
			sendBroadcast({'chatId': nickName, 'content': content});
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
            return '<div class="row alert alert-info"><div class="col_8">' +
            messageObj.content +
            '</div><div class="col_4 text-right"><small>[<b>' +
            messageObj.chatId +
            '</b>' +
            messageObj.time +
            ']</small>' +
            '</div></div>';
        }
		
		function showBroadcastMessage(message) {
            $("#content").html($("#content").html() + message);
            $("#clear").show();
        }
		
		function clearBroadcast() {
			$('#content').html("");
			$('#clear').hide();
		}
	
	</script>
</body>
</html>