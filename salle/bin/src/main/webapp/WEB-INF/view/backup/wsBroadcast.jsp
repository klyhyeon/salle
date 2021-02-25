<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Web socket Example</title>
	<link rel="stylesheet" href="/resources/css/wsBroadcast.css">
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
</head>
<body>
	<div class="container">
		<div class="title_text">
			<h2>Basic Web Socket</h2>
			<p>Sample of basic Websocket broadcast without STOMP & SockJS.</p>
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
							<input type="hidden" value="${login.getNickName()}" id="nickName">
						</div>
					</div>				
				</div>
			</div>
			
			<div class="col_6">
				<div id="content"></div>
			</div>
		</div>
	</div>
	
	<script>
		var ws;
		function setConnected(connected) {		
			console.log('setConnected succeed');
			$('#connect').prop('disabled', connected);
			$('#disconnect').prop('disabled', !connected);
			if (connected) {
				$('#sendMessage').show();
			} else {
				$('#sendMessage').hide();				
			}
		}
		
		function connect() {
			ws = new WebSocket("ws://localhost:8080/_chat");
			ws.onopen = function () {
				showBroadcastMessage('<div class="alert alert-success">Connected to Server</div>');
			};
			
			ws.onmessage = function (data) {
				showBroadcastMessage(createTextNode(data.data));				
			};
			console.log('connect function succeed');
			setConnected(true);
		}
		
		function disconnect() {
			console.log('disconnect invoked')
			if(ws != null) {
				ws.close();
				showBroadcastMessage('<div class="alert alert-warning">Disconnected from Server</div>');				
			}
			setConnected(false);
		}
		
		function send() {
			ws.send($('#nickName').val()+":");
			ws.send($('#message').val());
			var sendTime = new Date().toLocaleTimeString();
			ws.send(sendTime);
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
		
		
		function createTextNode(msg) {
            return '<div class="alert alert-info">' + msg + '</div>';
        }
		
		function showBroadcastMessage(message) {
            $("#content").html($("#content").html() + message);
        }
	
	</script>

</body>
</html>