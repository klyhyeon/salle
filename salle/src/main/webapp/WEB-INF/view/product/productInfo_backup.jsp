<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>${product.pr_title}</title>
	<link rel="stylesheet" href="/resources/css/productInfo.css">
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
</head>
<body>

	<%@include file="../home.jsp" %>	
	<div class="container_pr_img">
	<%-- <img>는 컨텐츠일 때 background-image와 중첩으로 쓰인다. 중요하기 때문 --%>
		<a class="prev" onclick="button_click(-1)">&#10094</a>
				<img src="${product.pr_img_1}"/>
		<a class="next" onclick="button_click(1)">&#10095</a>
	</div>
	
	<div class="container_info1">
		<div class="wrap_nickName_pr_region">
			<div class="nickName">
			<form id="nickName_form" action="/profile/${nickName}" method="POST">
				<a href="javascript:{}" onclick="formSubmit()">
					${nickName}
				</a> 
				<input type="hidden" name="pr_email" value="${product.pr_email}"/>
			</form>
			</div>
		
			<div class="pr_region">
				${product.pr_region} 
			</div>
		</div>
		
		<div class="buy_chat">
		<form id="chatSubmit_form" action="/chatMessage" method="GET">
			<a href="javascript:{}" onclick="chatSubmit()">
				<input type="hidden" name="nickName" value="${nickName}"/>
				<input type="hidden" name="pr_id" value="${product.pr_id}"/>
				<input type="hidden" name="pr_email" value="${product.pr_email}"/>
				<input type="hidden" name="pr_title" value="${product.pr_title}"/>
				<button id="btn_chat">
					채팅으로 거래하기
				</button>
			</a>
		</form>
		</div>
	</div>
	
	<script type="text/javascript">
		function formSubmit() {
			document.getElementById('nickName_form').submit();
		}		 	
		
	 	function chatSubmit() {
	 		document.getElementById('chatSubmit_form').submit();
	 	} 
	</script>


	<div class="container_info2">
			<div class="pr_title">
				${product.pr_title}
			</div>
			<div class="pr_category_reg_date">
				<spring:message code="${product.pr_category}"/>
				&nbsp;	
				${hoursFromUpload}시간 전
			</div>
			<div class="pr_price">
				${product.pr_price}원
			</div>
	</div>
	
	<p>
	<div class="container_info3">
		<div class="div_pr_detail">
			${product.pr_detail}
		</div>
	</div>
	</p>
		
	 <script type="text/javascript">
	 	//div_pr_img 생성
	 	$(document).ready(function imgAdd() {
	 		var i = 1;
		 	for (i = 1; i < 6; i++) {
		 		var iStr = String(i);
		 		console.log(iStr);
		 		switch (i) {
					case 1:
	 					var divStr1 = `<div class='div_pr_img' id='pr_img_1' style='background-image: url(${product.pr_img_1})'></div>`;
							
	 						$(".container_pr_img").append(divStr1);
						 	showSlide(currSlide);
						break;
					case 2:
	 					var divStr2 = `<div class='div_pr_img' id='pr_img_2' style='background-image: url(${product.pr_img_2})'></div>`;
						if('${product.pr_img_2}' != '') {
							$(".container_pr_img").append(divStr2);
						}
						break;
					case 3:
	 					var divStr3 = `<div class='div_pr_img' id='pr_img_3' style='background-image: url(${product.pr_img_3})'></div>`;
						if('${product.pr_img_3}' != '') {
							$(".container_pr_img").append(divStr3);
						}
						break;
					case 4:
	 					var divStr4 = `<div class='div_pr_img' id='pr_img_4' style='background-image: url(${product.pr_img_4})'></div>`;
						if('${product.pr_img_4}' != '') {
							$(".container_pr_img").append(divStr4);
						}
						break;
					case 5:
	 					var divStr5 = `<div class='div_pr_img' id='pr_img_5' style='background-image: url(${product.pr_img_5})'></div>`;
						if('${product.pr_img_5}' != '') {
							$(".container_pr_img").append(divStr5);
						}
						break;
						}
		 		}
		 	});//end imgAdd()
		 	
		 	//이미지 슬라이더
		 	var currSlide = 1;
		 	showSlide(currSlide);
		 	
		 	function button_click(num) {
		 		showSlide((currSlide += num))
		 	}
		 	

		 	function showSlide(num) {
		 		const slides = document.querySelectorAll(".div_pr_img");
		 		if(num > slides.length) {
		 			currSlide = 1;
		 		} else if (num < 1) {
					currSlide = slides.length;
				}
		 		var i;
		 		for (i = 0; i < slides.length; i++) {
		 			slides[i].style.display = "none";			
		 		} 
		 		slides[currSlide - 1].style.display="block";
		 	}



		 	</script>

</body>
</html>
