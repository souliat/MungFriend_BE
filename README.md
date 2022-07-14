README

# 🐶 멍친구

견주들의 산책 품앗이 사이트

제작 기간 : 2022.06.24 ~ 2022.08.05.(예정)

# :information_desk_person: 팀원 소개

🐩 : **BACK-END (3명)**  <a href="https://github.com/souliat/MungFriend_BE">back-end github</a>

 + 인기천 : 

 + 김형준 : 

 + 진용희 : 

🐕‍🦺 : **FRONT-END (2명)** 

 + 이영주 : 

 + 정예빈 :
 
# 📏 ERD
![멍친구 ERD (채팅 추가)](https://user-images.githubusercontent.com/82041804/178925767-b721bcee-3286-4fc9-9290-09025d168629.JPG)

# 🛠 서비스 아키텍처
![멍프렌드 통합 아키텍처](https://user-images.githubusercontent.com/82041804/178923080-3297f289-a65c-4a77-91c9-7a18403d9f6d.JPG)


# :dizzy: 핵심기능
> 1) 회원가입 / 로그인 / 본인 인증
 + JWT 인증 방식으로 로그인 구현
 + SSO (Oauth2.0) 소셜 로그인 카카오, 구글 로그인 구현
 + 휴대폰 문자로 전송된 인증번호로 본인 인증 구현

> 2) CRUD
 + 멍멍이 프로필
 + ** 멍친구 (산책 도우미) 모집 게시글
 + 마이페이지
 + 사용자 정보
 + 신청
 + 리뷰
 
> 3) 위치 기반 서비스 제공
 + 회원의 주소 상 위도 경도를 활용하여 본인의 위치와 거리가 가까운 게시글 순으로 조회하는 기능 구현

> 4) 매칭 서비스 제공
 + 매칭 완료 시 매칭된 신청자에게 휴대폰 문자와 이메일 알림 발송
 + 작성자와 신청자 간 1 : 1 채팅방 자동 개설
 + 작성자는 매칭 취소 가능
 
> 5) 채팅 서비스 제공
 + 매칭 시 채팅 서비스를 통해 당사자 간 원활한 소통이 가능하도록 구현
 
# :tv: 데모영상
<img src="https://img.shields.io/badge/YouTube-FF0000?style=flat&logo=YouTube&logoColor=white"/> 추후올릴예정

# :computer: 기술 스택 

#### IDE
  <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellij idea&logoColor=white"> <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=for-the-badge&logo=Visual Studio Code&logoColor=white">
  
#### Server 
  <img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white"> <img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">
  
#### CI / CD
  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
  
#### Framework
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white"> <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/Redux-764ABC?style=for-the-badge&logo=Redux&logoColor=white">

  
#### Language
  <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=CoffeeScript&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  
#### Database
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  
#### Tool
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/Git-00000?style=for-the-badge&logo=Git&logoColor=F05032]"/> <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white]"/>

# :key: 트러블 슈팅

