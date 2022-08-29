README

![멍친구 커버](https://user-images.githubusercontent.com/82041804/182148365-9317f9a8-d6bf-45c0-b742-5502fc02ac30.jpg)

# 🐶 [멍친구](https://mungfriend.com)



저희 팀은 5명의 견주가 합심하여 멍멍이들의 삶의 질을 높여줄 수 있는 서비스를 고민해봤습니다!

1. 매일 반려견과 산책하러 나가기 힘든 견주!

2. 혼자 산책하러 나가기 심심한 견주!
위와 같은 견주들을 위해 “산책 매칭 서비스(멍친구)”를 기획했고 구현했습니다.

때로는 나의 강아지 산책을 부탁하고, 때로는 내가 다른 견주의 강아지를 산책시켜주기도 하는 산책 품앗이가 저희 서비스의 핵심 가치입니다.

<a href="https://mungfriend.com"><img src="https://img.shields.io/badge/서비스 바로가기-FA5A30?style=for-the-badge&logo=&logoColor="/></a>
<a href="https://protective-iodine-bc7.notion.site/1911e7ebf67242bab9b4828c368b879e"><img src="https://img.shields.io/badge/브로셔 바로가기-4F65FF?style=for-the-badge&logo=Notion&logoColor=white"/></a>

제작 기간 : 2022.06.24 ~ 2022.08.05

# :information_desk_person: 팀원 소개

|이영주|정예빈|
|:-:|:-:|
|<img src="https://avatars.githubusercontent.com/u/100979254?v=4" width="200px" />|<img src="https://avatars.githubusercontent.com/u/103884098?v=4" width="200px" />|
|<span style="color:aqua">`FE`</span>|<span style="color:aqua">`FE`</span>|
|https://github.com/jjugwen|https://github.com/yebin76|


|인기천|진용희|김형준|
|:-:|:-:|:-:|
|<img src="https://velog.velcdn.com/images/rlafbf222/post/221e1cf8-6b85-4c7c-b838-02312ba89f67/image.png" width="200px" />|<img src="https://velog.velcdn.com/images/rlafbf222/post/c71d2572-aaf5-461c-b2bd-eff4515ec950/image.png" width="200px" />|<img src="https://avatars.githubusercontent.com/u/82041804?s=400&u=fbba7a990dde09c887a001f0d4e0f2465849e2b5&v=4" width="200px" />|
|<span style="color:lightgreen">`BE`</span>|<span style="color:lightgreen">`BE`</span>|<span style="color:lightgreen">`BE`</span>|
|https://github.com/souliat|https://github.com/YongHuiAA|https://github.com/Kim-HJ1986|
 
# 📏 ERD
![멍친구 최종 ERD](https://user-images.githubusercontent.com/82041804/187105260-b377d67c-0878-4697-955f-347150ad61ee.JPG)

# 🛠 서비스 아키텍처

<img width="1875" alt="멍친구 최종 아키텍쳐" src="https://user-images.githubusercontent.com/82041804/182296119-9fedd2b1-37d4-4153-8a34-b92a771ab91e.png">

# :dizzy: 핵심기능
> 1) 사용자 편의를 고려한 인증, 인가 방식 & 본인 인증
 + JWT를 통한 인증 / Spring Security를 통한 인가 구현
 + SSO (Oauth2.0) 소셜 로그인 카카오, 구글 로그인 구현
 + 휴대폰 문자로 전송된 인증 번호 (Redis에 저장 및 관리) 본인 인증 구현
 
> 2) 위치 기반 서비스 제공
 + 회원이 입력한 주소 상 위도 경도를 활용하여 본인의 위치와 거리가 가까운 게시글 순으로 조회하는 기능 구현

> 3) 매칭 서비스 제공
 + 매칭 완료 시 매칭된 신청자에게 휴대폰 문자와 이메일 알림 발송
 + 작성자와 신청자 간 1 : 1 채팅방 자동 개설
 + 작성자는 매칭 취소 가능
 
> 4) 채팅 서비스 제공
 + 개인 정보의 노출 없이 매칭 시 채팅 서비스를 통해 당사자 간 원활한 소통이 가능하도록 구현

# 💥 트러블 슈팅
- FrontEnd
    <details>
    <summary> 이미지 호스팅 관련 </summary>
    <div markdown="1">
    
    <br>
    
    <문제 상황>
    
     - 일부 이미지를 ifh(무료 호스팅 이미지)를 이용해 나온 url로 썼더니, 특정 환경에서 이미지가 불러와지지 않는 문제가 발생했다. 
     - 대부분의 이미지를 리액트 파일에 직접 추가하여 파일 용량이 무거워지는 문제가 있었다. 
     <br>

    <원인 추론>
     - Ifh(무료 이미지 호스팅 사이트)를 사용한 이미지는 250일 정도의 사용 기간이 제한되는 문제가 있고, HSTS(HTTP Strict Transport Security) 등의 일부 환경에서 Ifh 사이트 연결이 막혀 보이지 않음.(ifh.cc 사이트의 SSL인증서 유효성 문제인 듯)
     <br>

    <해결 방안>
     - 이미지를 AWS S3에 저장, 객체별 url을 생성하여 사용하였다. 리액트 파일에 저장한 이미지들은 삭제하여 파일 용량을 줄였으며, 이미지를 임포트했던 부분을 줄임으로써 코드량도 줄였다.

    + (이미지 보안 관련)
     - 사이트 내 이미지는 대부분 디자이너의 순수 창작물로 저작권을 보호하고, 이미지 사용의 무분별한 사용을 막기 위해 AWS S3주소 노출을 제한하기로 했다. ⇒ AWS S3주소 노출을 막기 위해 공통적인 url을 env에 넣었다. ⇒ 그러나, url이 깃허브 코드에서만 가려지고, 사이트에서 노출되는 건 마찬가지였다. ⇒ 이에, index.html에 우클릭 금지를 설정(*`oncontextmenu*="*return* false"`)하여, 이미지를 url로 가져가지 못하게 막았다.
    </div>
    </details>
        
- BackEnd
    <details>
    <summary> @ManyToOne이 초래한 N+1 문제 해결을 통한 TPS 성능 개선 (34.1% ~ 64.8% 개선) </summary>
    <div markdown="1">
    
    <br>
    
    <문제 상황>
    - JMeter로 부하 테스트를 하던 중, 가장 빈번하게 조회되는 ‘전체게시글조회'와 ‘거리순조회'의 TPS 성능이 저조하고 병목 현상이 존재함을 발견
    
    <br>
    
    <원인 추론>
    - Debugging을 통해 Post 객체를 조회할 때, ManyToOne으로 연관관계가 설정되어있는 모든 Member 객체를 조회하는 N+1 문제 발견
    - ManyToOne으로 연관관계가 설정되면 default로 eager loading이 되는 것으로 알고 있었지만, 여러 객체를 조회할 경우 N+1 문제가 발생한다는 문제 상황을 파악
    
    <br>
    
    <해결 방안>
    - @EntityGraph를 사용하여 attributePath를 지정한 객체가 쿼리에서 바로 사용될 수 있도록 했으며, Left Join Fetch을 사용하여 하나의 쿼리에 Left Outer Join으로 모든 것을 조회할 수 있도록 변경
    - 추가적으로 ‘전체게시글조회’의 경우 @Transactional을 적용하여 하나의 로직 내 save()가 별도의 EntityManager에 의해 관리되지 않고 하나의 작업 단위로 관리되도록 수정

    `JMeter 테스트 결과`
    
    <img src = 'https://user-images.githubusercontent.com/82041804/182143923-44085a14-33a1-4726-8c8c-e98f8a4927d9.png'>

    <img src = 'https://user-images.githubusercontent.com/82041804/182143856-60bcf3fe-6a68-47ee-8271-fc152c4191fc.png'>
    </div>
    </details>
    
    <details>
    <summary> 다중 @OneToMany가 초래한 MultipleBagFetchException </summary>
    <div markdown="1">
    
    <br>
    <문제 상황>
        
    - Member 객체에 @OneToMany 연관관계로 설정된 테이블이 4개 정도 있었는데 특정 상황에서 N+1 문제를 해결하고자 모든 객체를 Fetch Join으로 조회했으나 MultipleBagFetchException 발생
    
    <br>
    <원인 추론>

    - Member를 조회할 때 BagType (중복을 허용하는 Collection)의 Collection을 2개 이상 조회하려고 했기 때문에 발생
    
    <br>
    <해결 방안>

    - TPS 성능을 가장 많이 잡아먹는 Collection에 Fetch Join을 걸어줬으며, 동시에 hibernate의 default_batch_fetch_size 옵션을 사용하여 호출되는 쿼리의 수를 대폭 감소시킬 수 있었음
    </div>
    </details>
        
    <details>
    <summary> Concurrent Modification Exception 발생 및 해결 (학습에 초점) </summary>
    <div markdown="1">
    
    <br>
    
    <문제 상황>
    - Member에 @OneToMany로 Dog 객체가 연관 관계 설정 되어있는데, 동시에 멤버변수로 대표 반려견의 사진 url이 존재함
    - Member의 DogList를 불러와 반복문을 돌 경우 Concurrent Modification Exception 발생
    
    <br>
    
    <원인 추론>
    - 반복문을 돌며 사용자의 프로필 사진을, 사용자가 선택한 대표 반려견 사진으로 바꾸게 되면 해당 사용자의 다른 반려견들의 사용자 정보도 동시에 업데이트가 됨
    - 이 경우 Iterator의 expectedModcount는 아직 0으로 기대되는데, 반영된 modCount는 1로 바뀌며 다음 for문을 돌때 expectedModCount ≠ modCount가 되어
    Concurrent modification exception이 발생
    
    <br>
    
    <해결 방안>
    1. 따라서 사용자의 대표 멍멍이 사진을 바꿔주는 코드는 for문 밖으로 빼내어 오류 해결
    2. 이를 통해 반복문 내부에서 iterator의 attribute을 변경하는 코드가 있으면 안된다는 것을 학습
    </div>
    </details>
        
        
        
    <details>
    <summary> EC2 프리티어 서버 안정화 작업 </summary>
    <div markdown="1">
    
    <br>
    
    <문제 상황>
    - 백엔드의 배포 서버는 EC2 프리티어 서버로, 할당된 메모리 자원이 1GB에 불과한데 스프링 프로젝트만 배포했을 경우에도 이미 500MB 이상 사용되어 사용자가 많아질 경우 서버가 다운되는 문제가 예측됨
    
    <br>
    
    <해결 방안>
    - 메모리 용량을 초과하더라도 여유 용량을 사용할 수 있도록 2GB의 SWAP 메모리를 생성하고 할당
    
    <img src='https://user-images.githubusercontent.com/82041804/182146849-25f2ce17-82b7-4fce-9800-dfa46b1b33bc.png'>

    </div>
    </details>
        

# :tv: 데모영상
<a href="https://www.youtube.com/embed/1ap-UItobNo"><img src="https://img.shields.io/badge/YouTube-FF0000?style=flat&logo=YouTube&logoColor=white"/></a>

[![미리보기](https://i.ytimg.com/vi/1ap-UItobNo/maxresdefault.jpg)](https://www.youtube.com/embed/1ap-UItobNo)

# :computer: 기술 스택 

#### IDE
  <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=intellij idea&logoColor=white"> <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=for-the-badge&logo=Visual Studio Code&logoColor=white">
  
#### Language
  <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=CoffeeScript&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  
#### Framework / Library
 <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white"> <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white]"/> <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/Redux-764ABC?style=for-the-badge&logo=Redux&logoColor=white"> <img src="https://img.shields.io/badge/Axios-56347C?style=for-the-badge&logo=ReactOs&logoColor=white"/> <img src="https://img.shields.io/badge/StyledComponents-DB7093?style=for-the-badge&logo=styled-components&logoColor=white"/> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  
#### Security
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">
  
#### Database
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
  
#### Server 
  <img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white"> <img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">
  
#### CI / CD
  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> 
  
#### Tool
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/Git-00000?style=for-the-badge&logo=Git&logoColor=F05032]"/> <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white]"/> 
