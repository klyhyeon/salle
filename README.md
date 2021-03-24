## 중고거래 웹서비스 salle
> 채팅 기반의 중고거래 플랫폼

- Spring MVC 패턴과 REST API로 웹서비스 제작 및 AWS 서버 배포 진행
___
## 기술스택
- Backend: Spring boot | Java | MariaDB(AWS RDS) | AWS S3 | Mybatis
- Frontend: JavaScript(jQuery)
- Server: AWS EC2 | Ubuntu
___
## 참여
- 백호
___
## 기능구현 설명
___
### 회원관리(Member)
- 구조 : modelAttribute로 전달받은 Member객체 DB에 저장
- 비밀번호 암호화 : jasypt encryption
```
BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor(); 

    @Override
    public void insertMember(Member member) {
    	String rawPwd = member.getPassword();
    	String encryptedPwd = encryptor.encryptPassword(rawPwd);
    	member.setPassword(encryptedPwd);
        memberMapper.insertMember(member);
    }
```
___
### 상품등록(Product)
- 구조 : modelAttribute로 전달받은 Member객체 DB에 저장
- 한 버튼으로 이미지 여러개 저장 : Javascript FileReader, FormData 사용해 ajax로 비동기전송
```
 var img_count = 1;
 var formData = new FormData();

	$("#img").change(function fileadd() {
		var reader = new FileReader;
		var showImg = "<div class='pr_img_"+ (img_count)+"'><img id='img_"+(img_count)+"' src=''/><button type='button' class='button_img'    value='pr_img_"+(img_count)+"' onclick='deleteImg(this.value)'></button></div>";
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
    			processData: false,
    			contentType: false,
    			success: function(data) {
    				console.log('jQuery ajax form submit success');
    				}
    		}); //end ajax
    		
    	formData.delete;
	}
```
