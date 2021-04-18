<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!DOCTYPE html>

<html>
<head>
<!-- CSS -->
<link rel="stylesheet" href="/css/home.css">
<!-- JS, jQuery -->
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="js/home.js"></script>
<!-- fontAwesome -->  
<script src="https://use.fontawesome.com/46a3662715.js"></script>

<title>Salle Main</title>
</head>
<body>
	<c:set var="s3Url" scope="application" value="https://sallestorage.s3.ap-northeast-2.amazonaws.com/"></c:set>
	<header class="nav-bar">
			<div class="categoryicon-wrap">
				<input type="checkbox" id="categoryicon" onclick="showCategory()"> 	
				<label id="categoryicon-label" for="categoryicon"> 
					<span></span>
					<span></span> 
					<span></span>
				</label>
				<div class="category-list" id="category-list">
					<strong>카테고리</strong>
					<ul>
						<li><a href="<c:url value="/category/digital"/>"><spring:message code="digital"/></a></li>
						<li><a href="<c:url value="/category/furniture"/>"><spring:message code="furniture"/></a></li>
						<li><a href="<c:url value="/category/kids"/>"><spring:message code="kids"/></a></li>
						<li><a href="<c:url value="/category/lifestyle"/>"><spring:message code="lifestyle"/></a></li>
						<li><a href="<c:url value="/category/sports"/>"><spring:message code="sports"/></a></li>
						<li><a href="<c:url value="/category/womengoods"/>"><spring:message code="womengoods"/></a></li>
						<li><a href="<c:url value="/category/womenclothes"/>"><spring:message code="womenclothes"/></a></li>
						<li><a href="<c:url value="/category/menclothes"/>"><spring:message code="menclothes"/></a></li>
						<li><a href="<c:url value="/category/games"/>"><spring:message code="games"/></a></li>
						<li><a href="<c:url value="/category/beauty"/>"><spring:message code="beauty"/></a></li>
						<li><a href="<c:url value="/category/pets"/>"><spring:message code="pets"/></a></li>
						<li><a href="<c:url value="/category/books"/>"><spring:message code="books"/></a></li>
						<li><a href="<c:url value="/category/plants"/>"><spring:message code="plants"/></a></li>
						<li><a href="<c:url value="/category/etc"/>"><spring:message code="etc"/></a></li>
					</ul>
				</div>
			</div>
		<div class="nav-bar-wrap">
			<div class="logo-search-wrap">
				<a href="<c:url value="/"/>"> 
					<span class="logo-title">
						Salle?
					</span>
				</a>
				<form action="/search/result" method="GET">
					<input type="text" id="searchWord" name="searchWord" placeholder="검색어를 입력하세요"
						maxlength="50" size="60">
					<button class="searchButton">
						<img class="searchButtonImg" alt="Submit Form" src="https://sallestorage.s3.ap-northeast-2.amazonaws.com/static/img/searchicon.png"/>
					</button>
				</form>
			</div>
			<div class="member-info-wrap">
					<a class="sell" href="<c:url value="/product/register"/>"> 
						판매하기
					</a>
				<c:choose>
					<c:when test="${login == null}">
						<a class="login" href="<c:url value="/login"/>"> 
							로그인/회원가입
						</a>
					</c:when>
					<c:otherwise>
						<button id="link_logininfo" >  
							${login.getNickName()}님
						</button>
						<input type="hidden" value="${login.getEmail()}" id="emailInput">
						<a href="<c:url value="/profile/${login.getNickName()}"/>">
							프로필
						</a>&nbsp;
						<a href="<c:url value="/chatList"/>">
							채팅
							<span id="messageAlert"></span>
						</a>
					<div class="member-loggedin">
						<ul id="link_logintoggle">
							<li>
								<a href="/logout">
									로그아웃
								</a>
							</li>				
						</ul>
							<script type="text/javascript">

									$("#link_logininfo").click(function() {
										$("#link_logintoggle").toggle();
									});

									var loginEmail = document.getElementById('emailInput').value;
										console.log("loginEmail: " + loginEmail);
									
									$(document).ready(function() {
									
										if (loginEmail != null) {
											getUnread();
											getInfiniteUnread();
										}
									});
									
									
									<%-- function getUnread() {
										$.ajax({
											url: "/chatUnreadAlert/ajax",
											type: "POST",
											data: JSON.stringify({
												email: loginEmail
											}) ,
											dataType: "json",
											contentType: "application/json",
											success: function(result) {
												if (result >= 1) {
													showUnread(result);
												} else {
													showUnread('');
												}
											}
										});
									}
									
									function getInfiniteUnread() {
										setInterval(() => {
											getUnread();
										}, 1000);
									}
									
									function showUnread(result) {
										$('#messageAlert').html(result);
									}
									--%>
							</script>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
	</div>
	</header>
		

	

</body>
</html>