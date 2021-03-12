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
	<link rel="stylesheet" href="/resources/css/productEdit.css">
	<!-- jQuery -->
	<script src="https://code.jquery.com/jquery-3.5.1.js">
	</script>

</head>
<body>

	<%@include file="../home.jsp" %>


    <form:form action="save" method="post" id="form" enctype="multipart/form-data" modelAttribute="product">
    <form:input type="hidden" value="${product.pr_id}" id="pr_id" path="pr_id"/>
    <section class="pr_img">
  		<p>	
    		<label for="img"><h2>상품 이미지</h2></label>
    	</p>
	    	<input type="file" id="img" name="pr_img_files"/>	    	
	        	<div class="wrap_pr_img">
		    	<c:forEach var="img" items="${imgList}" varStatus="loop">			
			    	<div class="pr_img_${loop.index}" id="imgEx">
			    		<img id="pr_img" src="https://sallestorage.s3.ap-northeast-2.amazonaws.com/${img}" width="150px" height="150px"/>
			    		<button type="button" class="button_img" value="pr_img_${loop.index}" onclick="deleteImgEx(this.value)"></button>
			    	</div>
		    	</c:forEach>
    	</div>
	    <form:errors id="errors" path="pr_img_1"/>
	    <p></p>
	    <p></p>
    	<input type="button" id="button" value="이미지 확정하기" onclick="fileUpload()"/>
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
	   		<textarea id="pr_detail" placeholder="상품 설명을 입력하세요. 최대 500자" maxlength="1000" rows="10" cols="80" name="pr_detail" ></textarea>
		    <form:errors id="errors" path="pr_detail"/>
		</label>
	    </p>
    </section>
    	<input type="button" id="button" value="등록하기" onclick="submit()"/>
    </form:form>
    
    <!-- Javascript -->
    <!-- <script type="text/javascript" scr="/resources/static/js/sell.js"></script> -->
    <!-- Daum 주소 api -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js">
    </script>
    <script type="text/javascript">
    
    var img_count = 10;
    var formData = new FormData();
    var pr_id = document.getElementById('pr_id').value;
	    
    //pr_img
	//input 파일첨부 버튼 클릭하면 실행되는 change 메서드
	$("#img").change(function fileadd() {
		var reader = new FileReader;
	//이미지 파일 정보와 화면출력을 위해 <img> 태그를 변수로 만듦
		var str = "<div class='pr_img_"+ (img_count)+"'><img id='img_"+(img_count)+"' src=''/><button type='button' class='button_img' value='pr_img_"+(img_count)+"' onclick='deleteImgNew(this.value)'></button></div>";
	//파일 경로에 넣기 위해 String으로 변환시켜줌
		var img_count_string = img_count.toString();
		
	//jQuery append 메서드를 사용해 <div id="pr_img"> 안에 <img> 태그 변수를 추가해줌
		$(".wrap_pr_img").append(str);
	
	//formdata에 append
	
	//onload는 파일이 업로드 완료된 시점에 function을 발생시키는 메서드
	//<img src=""> 사용자가 업로드한 이미지 파일 경로를 src로 저장해줌(data.target.result) 
		reader.onload = function(data) {
	//태그 안의 속성을 입력할 수 있는 jQuery attr 메서드를 사용 
			$('#img_' + img_count_string).attr('src', data.target.result).width(150).height(150);
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
    
	function fileUpload() {
		var imgExArr = Array.from(document.querySelectorAll('div > img'));
		
		$.ajax({
    		url:"/productEdit/ajax",
   			type: 'POST',
   			data: JSON.stringify({
   				exImgArr: imgExArr,
   				pr_id: pr_id
   			}),
    		dataType: "json",
    		contentType: "application/json",
    		processData: false,
    			success: function() {
    				console.log('jQuery ajax form submit success');
    				$.ajax({
    					url:"/productEditImg/ajax",
    					type:"POST",
    					data: formData,
    					processData: false,
    					success: function() {
    						console.log("formData ajax submit success");
    					}
    				});
    			}
    		}); //end ajax		
    	formData.delete;
	}
	
	function submit() {
		$('#form').submit();		
}
	
	//delete img existing file
	function deleteImgEx(val) {
			//formData.delete(val);
			console.log('deleteImgEx(): ' + val);
			$('.' + val).remove();	
		}
	
	//delete img new file
	function deleteImgNew(val) {
		console.log("ajaxDelete running");
		
			formData.delete(val);

			console.log('deleteImgNew(): ' + val);
			$('.' + val).remove();	
		
		}

	//pr_price
	function priceCommas(x) {
	    	//입력값 가공
	    	
	    	x = x.replace(/[^0-9]/ig,'');
	    	x = x.replace(/,/ig,'');
	    	
	    	commaX = x.replace(/\B(?=(\d{3})+(?!\d))/g,',');
	    	
	    	$('#pr_price').val(commaX);
	    }
		
	//daum 주소 api
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