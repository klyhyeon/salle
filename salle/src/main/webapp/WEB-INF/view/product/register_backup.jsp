<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<!-- .jsp <head> 통일시켜주기 위해서 주석처리(12/29)
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
-->
    <title>판매하기</title>
	<!-- CSS -->
	<link rel="stylesheet" href="/resources/css/sell.css">
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js">
	</script>

</head>
<body>

	<%@include file="../home.jsp" %>


    <form:form action="done" method="post" enctype="multipart/form-data" modelAttribute="product">
    
    <section class="pr_img">
  		<p>	
    		<label for="img"><h2>상품 이미지</h2></label>
    	</p>
    	<div id="pr_img">
	    	<input type="file" id="img" name="pr_img_files"/>	    	
	    
    	</div>
	    <%=request.getRealPath("/") %>
	    <input id="upload" type="button" value="업로드" onclick="fileUpload()"/>
	    <form:errors id="errors" path="pr_img_1"/>
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
	    	<p>
	    		<!-- <form id="form" name="form" method="post"> -->
		    		<input type="button" onclick="goPopup()" value="주소검색">
		    </p>
					우편번호<input type="text" id="zipNo" name="zipNo" /><br>
				    전체주소 <input type="text" id="roadFullAddr" name="roadFullAddr" /><br>
				    도로명주소 <form:input type="text" id="roadAddrPart1" name="pr_region" path="pr_region"/>
				    <form:errors id="errors" path="pr_region"/>
				    <br>
					상세주소<input type="text" id="addrDetail" name="addrDetail" /><br> 
					참고주소<input type="text" id="roadAddrPart2" name="roadAddrPart2" /><br>
				<!--</form> -->
	    </p>
    </section>
    
    <section class="pr_quality">
    	<p>
	    	<p>
		    	<h2>상품 상태</h2>
		    </p>	    	
	    		<input type="radio" id="pr_quality" name="pr_quality" value="상" />
	    		<label for="pr_quality">상</label>
	    	
	    
	    		<input type="radio" id="pr_quality" name="pr_quality" value="중" />
	    		<label for="pr_quality">중</label>
	    	
	    	
	    		<input type="radio" id="pr_quality" name="pr_quality" value="하" />
	    		<label for="pr_quality">하</label>
			    <form:errors id="errors" path="pr_quality"/>
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
	   		<textarea id="pr_detail" placeholder="상품 설명을 입력하세요. 최대 500자" maxlength="1000" rows="10" cols="80" name="pr_detail"></textarea>
		    <form:errors id="errors" path="pr_detail"/>
		</label>
	    </p>
    </section>
    
	<input type="submit" value="등록하기" /> 
    </form:form>
    
    <!-- Javascript -->
    <!-- <script type="text/javascript" scr="/resources/static/js/sell.js"></script> -->
    <script>
    
    var img_count = 1;
    var formData = new FormData();
	    
    //pr_img
	//input 파일첨부 버튼 클릭하면 실행되는 change 메서드
	$("#img").change(function fileadd() {
		var reader = new FileReader;
	//이미지 파일 정보와 화면출력을 위해 <img> 태그를 변수로 만듦
		var str = "<img id='img_"+(img_count)+"' src=''/>";
	//파일 경로에 넣기 위해 String으로 변환시켜줌
		var img_count_string = img_count.toString();
		
	//jQuery append 메서드를 사용해 <div id="pr_img"> 안에 <img> 태그 변수를 추가해줌
		$("#pr_img").append(str);
	
	//formdata에 append
	
	//onload는 파일이 업로드 완료된 시점에 function을 발생시키는 메서드
	//<img src=""> 사용자가 업로드한 이미지 파일 경로를 src로 저장해줌(data.target.result) 
		reader.onload = function(data) {
	//태그 안의 속성을 입력할 수 있는 jQuery attr 메서드를 사용 
			$('#img_' + img_count_string).attr('src', data.target.result).width(150);
		};
		
	//화면에 이미지를 출력해주는 FileReader 객체 인스턴스 reader.readAsDataURL();
	//this.files는 <input type="file">을 통해 업로드한 파일의 정보를 저장하고 있는 배열이다.
	//첨부하기 1회당 file 하나만 업로드해서 <img_0,1,2...>에 각각의 파일들을
	//할당시켜줄 것이기 때문에 files[0]로 index 고정	

		reader.readAsDataURL(this.files[0]);
	
	//ajax로 전달할 files를 formData에 담는다.  
		formData.append('pr_img_' + img_count_string, this.files[0]);

	img_count++;
			
    });
    
    //업로드 버튼 클릭
    //var xmlReq = new XMLHttpRequest();
	function fileUpload() {
		
		$.ajax({
    		url:"/sell/ajax",
   			type: 'POST',
    		data: formData,
    			processData: false,
    			contentType: false,
    			success: function(data) {
    				console.log('jQuery ajax form submit success');
    				}
    		}); //end ajax
    		
    	formData.delete;
	}

	//pr_price
	function priceCommas(x) {
	    	//입력값 가공
	    	
	    	x = x.replace(/[^0-9]/ig,'');
	    	x = x.replace(/,/ig,'');
	    	
	    	commaX = x.replace(/\B(?=(\d{3})+(?!\d))/g,',');
	    	
	    	$('#pr_price').val(commaX);
	    }

	//pr_region
	function goPopup() {
			
			var pop = window.open("/sell/region","pop","width=570, height=420, scrollbars=yes, resizable=yes");
		} 	
		//주소입력창
		function jusoCallBack(roadFullAddr,roadAddrPart1,addrDetail,roadAddrPart2, zipNo){ 
			// 2017년 2월 제공항목이 추가되었습니다. 원하시는 항목을 추가하여 사용하시면 됩니다. 
			document.form.roadFullAddr.value = roadFullAddr; 
			document.form.roadAddrPart1.value = roadAddrPart1; 
			document.form.roadAddrPart2.value = roadAddrPart2; 
			documentform.addrDetail.value = addrDetail; 
			document.form.zipNo.value = zipNo; 
		};
    </script>
   

</body>
</html>