<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ProductList</title>
<link rel="stylesheet" href="/css/searchResultZero.css">
</head>
<body>

<%@include file="../home.jsp" %>

	<div>
		<h2>요청하신 검색어 <span>"${searchWord}"</span> 결과가 없습니다.</h2>
	</div>

</body>
</html>