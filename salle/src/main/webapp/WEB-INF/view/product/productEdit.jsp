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
	<link rel="stylesheet" href="/css/productRegister.css">
	<!-- jQuery -->


</head>
<body>

	<%@include file="add.jsp" %>
	<input type="hidden" id="s3Url" value="${s3Url}"/>
    
    <!-- Javascript -->
    <!-- <script type="text/javascript" scr="/resources/static/js/sell.js"></script> -->
    <!-- Daum 주소 api -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js">
    </script>
    <script src="https://code.jquery.com/jquery-3.5.1.js">
	</script>
    <script type="text/javascript">
    
    var img_count = 10;
    var formData = new FormData();
    var pr_id = document.getElementById('pr_id').value;
    var s3Url = document.getElementById('s3Url').value;
    
    $(document).ready(
    	$.ajax({
    		url: "/get/imgList/"+pr_id,
    		type: "GET",
    		success: function(data) {
	    		var imgListArr = JSON.parse(data);
	    		if (imgListArr != null) {		
	    			var idx = 0;
	    			for (idx = 0; idx < imgListArr.length; idx++) {
	    				let div = document.createElement("div");
	    				div.className = "pr_img_" + idx;
	    				div.id = "imgEx";
						document.getElementByClassName('wrap_pr_img').appendChild(div);
						let img = document.createElement("IMG");
						img.id = "pr_img_ex";
						img.src = s3Url + data[0];
						document.getElementByClassName('pr_img_' + idx).appendChild(img);
	    			}
    			}
    		}
    	})//end ajax
    );//end $document ready
	    
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
		var imgExArr = Array.from(document.querySelectorAll("#pr_img_ex"));
		if (imgExArr.length != 0) {
			var imgExSrcArr = new Array(); 
			for (var i = 0; i < imgExArr.length; i++) {
				imgExSrcArr.push(imgExArr[i].src);
				console.log("imgEx: " + imgExArr[i].src);
			}
	    		formData.append("imgExArr", imgExSrcArr);
		}
	    		formData.append("pr_id", pr_id);
	    	var xhttp = new XMLHttpRequest();
	    	xhttp.open("POST", "/productEdit/ajax", true);
	    	xhttp.onload = function() {
	    		console.log("status" + xhttp.status);
	    	}
	    	xhttp.send(formData);
	    	
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