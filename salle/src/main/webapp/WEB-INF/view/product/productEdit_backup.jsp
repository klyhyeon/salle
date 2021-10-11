<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>

    <title>판매하기</title>
	<!-- CSS -->
	<link rel="stylesheet" href="/resources/css/productEdit.css">
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js">
	</script>

</head>
<body>

	<%@include file="../home.jsp" %>


    <form:form action="save" method="post" enctype="multipart/form-data" modelAttribute="product">
    
    <section class="pr_img">
  		<p>
    		<label for="img"><h2>상품 이미지</h2></label>
    	</p>
	    	<input type="file" id="img" name="pr_img_files"/>
    	<div class="wrap_pr_img">
		    	<c:forEach var="img" items="${imgList}">			
			    	<div class="pr_img" id="${img}">
			    		<img id="pr_img_img" src="${img}" width="150px" height="150px"/>
			    		<input type="button" class="button_img" value="${img}" onclick="deleteImg(this.value)"/>
			    	</div>
		    	</c:forEach>
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
		    <form:select id="pr_category" name="pr_category" required="required" path="pr_category">
		    	<form:option value="digital"><spring:message code="digital"/></form:option>
		    	<form:option value="furniture"><spring:message code="furniture"/></form:option>
		    	<form:option value="kids"><spring:message code="kids"/></form:option>
		    	<form:option value="lifestyle"><spring:message code="lifestyle"/></form:option>
		    	<form:option value="sports"><spring:message code="sports"/></form:option>
		    	<form:option value="womengoods"><spring:message code="womengoods"/></form:option>
		    	<form:option value="womenclothes"><spring:message code="womenclothes"/></form:option>
		    	<form:option value="menclothes"><spring:message code="menclothes"/></form:option>
		    	<form:option value="games"><spring:message code="games"/></form:option>
		    	<form:option value="beauty"><spring:message code="beauty"/></form:option>
		    	<form:option value="pets"><spring:message code="pets"/></form:option>
		    	<form:option value="books"><spring:message code="books"/></form:option>
		    	<form:option value="plants"><spring:message code="plants"/></form:option>
		    	<form:option value="etc"><spring:message code="etc"/></form:option>
			</form:select>
	    </p>
    </section>
    
        <section class="pr_region">
	    <p>
	    	<h2>거래지역</h2>
	    </p>
	    <p>
		<input type="button" id="button" onclick="daumPostcode()" value="주소검색"><br>
		<form:input type="text" id="addr" placeholder="주소" width="200" path="pr_region"/>
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
	   		<form:textarea id="pr_detail" placeholder="상품 설명을 입력하세요. 최대 500자" maxlength="1000" rows="10" cols="80" name="pr_detail" path="pr_detail"></form:textarea>
		    <form:errors id="errors" path="pr_detail"/>
		</label>
	    </p>
    </section>
    
	<input type="submit" value="저장하기" /> 
    </form:form>
    
    <!-- Javascript -->
    <!-- <script type="text/javascript" scr="/resources/static/js/sell.js"></script> -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js">
    </script>
    <script type="text/javascript">
    
    var img_count = 1;
    var formData = new FormData();
	    
    //pr_img
	//input 파일첨부 버튼 클릭하면 실행되는 change 메서드
	$(documnet).ready(function fileadd() {
		var reader = new FileReader;
	//이미지 파일 정보와 화면출력을 위해 <img> 태그를 변수로 만듦
	var i = 1;  
		while(i < 6) {
			switch (i) {
			case 1:
				if (${product.pr_img_1} != null) {
					var str = "<img id='img_1' src='${product.pr_img_1}'/>";
				}
				break;
			}
			
			i++;
		}
		
	//img edit
		var imgTarget = document.getElementByClass('button_img');
		imgTarget.addEventListener("click", deleteImg(imgTarget.value));
	
		function deleteImg(val) {
		console.log('deleteImg(): ' + val);
		$('#' + data).remove();	
		}

		//jQuery append 메서드를 사용해 <div id="pr_img"> 안에 <img> 태그 변수를 추가해줌
		$("#pr_img").append(str);
	
	//onload는 파일이 업로드 완료된 시점에 function을 발생시키는 메서드
	//<img src=""> 사용자가 업로드한 이미지 파일 경로를 src로 저장해줌(data.target.result) 
		var img_count_string = img_count.toString();
		reader.onload = function(data) {
	//태그 안의 속성을 입력할 수 있는 jQuery attr 메서드를 사용 
			$('#img_' + img_count_string).width(150);
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
	
    </script>
   

</body>
</html>