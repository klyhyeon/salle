<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Profile</title>
<link rel="stylesheet" href="/resources/css/profile.css">
</head>
<body>

<%@include file="home.jsp" %>

<div class="wrap_profile">
	<div class="member_info">
		<div class="nickName">
			<h1>${nickName}</h1>
		</div>
		<div class="productCount">
			<h2>총 판매상품: ${totalProduct} 건</h2>
			<h2>총 판매완료: ${totalSold} 건</h2>
		</div>
	</div>

	<c:forEach var="product" items="${sellerProductList}">
		<div class="wrap_productInfo">
			<div class="wrap_img">
				<img src="${product.pr_img_1}" class="pr_img"/>
			</div>
			<div class="wrap_title_price_hours">
				<a href="<c:url value="/productInfo/${product.pr_id}"/>">
					<span class="pr_title">${product.pr_title}</span><br>
					<span class="pr_price">${product.pr_price}원</span><br>
					<span class="hoursFromUpload">${product.hoursFromUpload}시간 전</span>
				</a>
			</div>
			<c:choose>
				<c:when test="${login.email == product.pr_email}">
				<div class="wrap_update">
					 <a href="<c:url value="/product/${product.pr_id}/edit"/>">  
						<button id="update">
							수정
						</button>
					</a> 	
				</div>	
				<div class="wrap_delete">
				 <a href="<c:url value="/product/${product.pr_id}/delete"/>">
				 <input type="hidden" id="pr_img" value="${product.pr_id}"> 
						<button id="delete" onclick="deleteAlert()">
							삭제
						</button>
				</a>
				</div>	
				</c:when>
				<c:otherwise>
				</c:otherwise>				
			</c:choose>
		</div>
	</c:forEach>
</div>

	<script type="text/javascript">
	
			function deleteAlert() {
				var flag = "false"; 
				var result = confirm("정말 삭제하시겠습니까?");
				
				console.log('result: ' + result); 

				if (result) {
					flag = "true";
				}
					
				console.log('flag: ' + flag); 
				
				$.ajax({
					url:'/ajax/delete',
					type: 'POST',
					data: JSON.stringify({
						flag: flag
					}),
					dataType: 'json',
					//magic setting resolved an error
					contentType: 'application/json',
					success: function(data) {
						console.log('jQuery ajax delete success');
					}
				});
			}
			
	</script>


</body>
</html>