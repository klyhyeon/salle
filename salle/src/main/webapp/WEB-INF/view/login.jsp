<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="/resources/css/login.css">
    <title>로그인</title>
</head>
<body>

	<div>
    <form:form action="/login/done" method="post" modelAttribute="login">
    <p>
        <label>
        이메일:
        <input type="text" name="email" placeholder="이메일을 입력하세요"/>
        </label>
    </p>

    <p>
        <label>
        비밀번호:
        <input type="password" name="password"/>
        </label>
    </p>
    <input type="submit" value="로그인">
    <input type="button" value="회원가입" onclick="location.href='/register/main'">
    </form:form>
    </div>
</body>
</html>