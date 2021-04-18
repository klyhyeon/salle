<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>CategoryProductList</title>
<link rel="stylesheet" href="css/productList.css">
</head>
<body>

<%@include file="../home.jsp" %>

	<article class="article_product">
	<c:forEach var="product" items="${categoryProductList}">
	<div class="container_product">
	<a href="<c:url value="/productInfo/${product.pr_id}"/>">
		<div class="div_pr_img">
			<img src="${s3Url}${product.pr_img_1}" class="pr_img"/>
		</div>
		<div class="div_pr_title">
			${product.pr_title}
		</div>	
		<div class="div_pr_price">
			${product.pr_price}원
		</div>
	</div>
	</a>
	</c:forEach>
	</article>

</body>
</html>