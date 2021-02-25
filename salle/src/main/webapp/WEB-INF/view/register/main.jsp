<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html>
<head>
    <title>회원가입</title>
    <link rel="stylesheet" href="/resources/css/register_main.css">
</head>
<body>

    <div class="container">
    	<div class="header">
    		<h2>회원가입</h2>
    	</div>
	    <form:form class="form" action="done" method="post" modelAttribute="member">
    	<div class="register_info">
	        <label>이메일</label>
	        <form:input type="text" path="email"/>
	        <form:errors class="errors" path="email"/>	       
	    </div>
		<div class="register_info">
	        <label>비밀번호</label>
	        <form:input type="password" path="password"/>
	        <form:errors class="errors" path="password"/>
		</div>
		<div class="register_info">
	        <label>비밀번호 확인</label>
	        <form:input type="password" path="confirmPassword"/>
	        <form:errors class="errors" path="confirmPassword"/>
	        
		</div>
		<div class="register_info">
	        <label>이름</label>
	        <form:input type="text" path="name" />
	        <form:errors class="errors" path="name"/>
		</div>
		<div class="register_info">
	        <label>닉네임</label>
	        <form:input type="text" path="nickName" />    
	        <form:errors class="errors" path="nickName"/>
		</div>
		
		<div class="register_info">
	        <label>휴대전화</label>
	        <input type="text" name="phoneNum" />
	        <form:errors class="errors" path="phoneNum"/>
    	</div>
    <input class="submit" type="submit" value="가입하기">
    </form:form>
	</div>
</body>
</html>