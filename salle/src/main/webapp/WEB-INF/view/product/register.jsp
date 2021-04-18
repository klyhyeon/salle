<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <title>판매하기</title>
	<link rel="stylesheet" href="css/productRegister.css">
	<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
</head>

<body>
	<%@include file="../home.jsp" %>

    <form:form action="done" method="post" enctype="multipart/form-data" id="form" modelAttribute="product">
    <section class="pr_img">
  		<p>	
    		<label for="img"><h2>상품 이미지</h2></label>
    	</p>
	    	<input type="file" id="img" name="pr_img_files"/>	    	
    		<div class="wrap_pr_img">
    		
    		</div>
	    <form:errors id="errors" path="pr_img_1"/>
	    <p></p>
	    <p></p>
    	<%-- <input type="button" id="btn_img" value="이미지 확정하기" onclick="fileUpload()"/> --%>
    </section>
    
    <section class="pr_title">
	    <p>
	    	<h2>제목</h2>
	   		<form:input type="text" name="pr_title" size="50" path="pr_title"/>
	   		<form:errors id="errors" path="pr_title"/>
	    </p>
    </section>

    <section class="pr_category">
	    <p>
	    	<h2>상품 카테고리</h2>
		    <select id="pr_category" name="pr_category" required>
		    	<option value="digital"><spring:message code="digital"/></option>
		    	<option value="furniture"><spring:message code="furniture"/></option>
		    	<option value="kids"><spring:message code="kids"/></option>
		    	<option value="lifestyle"><spring:message code="lifestyle"/></option>
		    	<option value="sports"><spring:message code="sports"/></option>
		    	<option value="womengoods"><spring:message code="womengoods"/></option>
		    	<option value="womenclothes"><spring:message code="womenclothes"/></option>
		    	<option value="menclothes"><spring:message code="menclothes"/></option>
		    	<option value="games"><spring:message code="games"/></option>
		    	<option value="beauty"><spring:message code="beauty"/></option>
		    	<option value="pets"><spring:message code="pets"/></option>
		    	<option value="books"><spring:message code="books"/></option>
		    	<option value="plants"><spring:message code="plants"/></option>
		    	<option value="etc"><spring:message code="etc"/></option>
			</select>
	    </p>
    </section>
    
    <section class="pr_region">
	    <p>
	    	<h2>거래지역</h2>
	    </p>
	    <p>
			<input type="button" id="button" onclick="daumPostcode()" value="주소검색"><br>
			<form:input type="text" id="addr" placeholder="주소" width="200" path="pr_region1"/><br><br>
			<form:input type="text" id="addr" placeholder="상세주소" width="200" path="pr_region2"/>
	    </p>
    </section>
    
    <section class="pr_price">
	    <p>
	    	<h2>상품 가격</h2>
	   		<form:input type="text" id="pr_price" onkeyup="priceCommas(this.value)" name="pr_price" path="pr_price"/>원
		    <form:errors id="errors" path="pr_price"/>
	    </p>
    </section>

    <section class="pr_detail">
	    <p>
	    <label>
	    	<h2>상품 설명</h2>
	   		<textarea id="pr_detail" placeholder="상품 설명을 입력하세요. 최대 500자" maxlength="1000" rows="10" cols="80" name="pr_detail" ></textarea>
		    <form:errors id="errors" path="pr_detail"/>
		</label>
	    </p>
    </section>       	
    	<input type="button" id="btn_submit" value="등록하기" onclick="fileUpload()"/>
    </form:form>
    
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script type="text/javascript">
    var img_count = 1;
    var formData = new FormData();
	$("#img").change(function fileadd() {
		var reader = new FileReader;
		var showImg = "<div class='pr_img_"+ (img_count)+"'><img id='img_"+(img_count)+"' src=''/><button type='button' class='button_img' value='pr_img_"+(img_count)+"' onclick='deleteImg(this.value)'></button></div>";
		var img_count_string = img_count.toString();
		$(".wrap_pr_img").append(showImg);
		reader.onload = function(data) {
			$('#img_' + img_count_string).attr('src', data.target.result).width(150).height(150);
		};
		reader.readAsDataURL(this.files[0]); 
		formData.append('pr_img_' + img_count_string, this.files[0]);
		img_count++;
    });
    
	function fileUpload() {
		$.ajax({
    		url:"/productReg/ajax",
   			type: 'POST',
    		data: formData,
   			contentType: 'multipart/form-data'
<%--   			complete: function(data) {
   				console.log('jQuery ajax form submit success');
   				if (data == "success")
   					submit();
   				},
   			error: function() {
   				alert("submit error");	
   			}
--%>
   		})//end ajax
   		.done(function (data) {
			if (data == "success") {
				submit();
			} else {
				alret("submit error");
			}
		}); 
    	formData.delete;
	}
	
	function submit() {
    		$('#form').submit();		
	}

	function priceCommas(x) {
	    	x = x.replace(/[^0-9]/ig,'');
	    	x = x.replace(/,/ig,'');
	    	commaX = x.replace(/\B(?=(\d{3})+(?!\d))/g,',');
	    	$('#pr_price').val(commaX);
	    }
		
   	function daumPostcode() {
   		new daum.Postcode({
   			oncomplete: function(data) {
    		var addr = '';
   			if (data.userSelectedType === 'R') {
   				addr = data.roadAddress;
   			} else {
   				addr = data.jibunAddress; 
  				}
    		document.getElementById('addr').value = addr;
  			}
  		}).open();
  	}
	
	function deleteImg(val) {
			formData.delete(val);
			console.log('deleteImg(): ' + val);
			$('.' + val).remove();	
		}
    </script>
   

</body>
</html>