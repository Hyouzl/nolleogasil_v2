<div align="center">

<img src="https://github.com/fourroro/nolleogasil_backend/blob/chon/README_images/banner.jpg" alt="놀러가실?" width="600px" />


사용자의 취향에 따라 여행경로를 추천해주고, 맛집에 동반할 메이트를 매칭해주는 웹 서비스입니다.

![Java](https://github.com/user-attachments/assets/2d7a2ac4-8142-4f00-9a04-eb7d1c97c499) 
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flate&logo=SpringBoot&logoColor=white) 
![Gradle](https://github.com/user-attachments/assets/f6071a6a-e644-4e28-a95e-3dfcfced44b7) 
![React](https://img.shields.io/badge/React-61DAFB?style=flate&logo=React&logoColor=white) 
![JS](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white) 
<img src="https://img.shields.io/badge/WebSocket-2072EF?style=flat&logoColor=white"> 
<img src="https://img.shields.io/badge/STOMP-41454A?style=flat&logoColor=white"> 
<img src="https://img.shields.io/badge/Kakao Login API-FFCD00?style=flat&logo=Kakao&logoColor=white"> 


<img src="https://img.shields.io/badge/Kakao Map API-FFCD00?style=flat&logo=Kakao&logoColor=white"> 
<img src="https://img.shields.io/badge/OpenAI API-412991?style=flat&logo=OpenAI&logoColor=white"> 
<img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=flat&logo=rabbitmq&logoColor=white"/> 
<img src="https://img.shields.io/badge/AWS-FF9900?style=flat&logo=amazonec2&logoColor=white"/> 
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white"/> 
<img src="https://img.shields.io/badge/Jenkins-E06666?style=flat&logo=jenkins&logoColor=white"/> 
<img src="https://img.shields.io/badge/Nginx-67c83b?style=flat&logo=nginx&logoColor=white"/> 
<img src="https://img.shields.io/badge/ORACLE-F80000?style=flat&logo=oracle&logoColor=white"/> 
<img src="https://img.shields.io/badge/Redis-cc0000?style=flat&logo=redis&logoColor=white"/> 


</div>

# 🖥 프로젝트 소개


+ 프로젝트 이름: 놀러가실?
+ 개발기간: 2024/01/05 ~ 2024/06/20
+ 개발 배경 및 목적
  * 여행을 가려면 인터넷을 일일이 검색하면서 일정을 계획해야 한다. 그러다보면 짧게는 하루, 길게는 며칠의 시간을 소비하게 된다. 여행 준비 과정에서의 번거로움을 줄이고 시간을 절약하고자 ChatGPT를 이용한 사용자 맞춤 여행 일정 제공 서비스를 고안했다.
  * COVID-19 이후, 혼자 식사를 하는 일이 많아지면서 일명 ‘혼밥’을 하는 사람들이 증가했다. 그러나 아직 혼밥을 어려워하는 사람들은 이와 같은 상황이 생기면, 굶거나 커피로 끼니를 때우곤 한다. 이러한 사람들을 위해 함께 식사할 수 있는 맛집메이트 매칭 서비스를 고안했다.
+ 주요 기능
  * 로그인 및 회원가입
  * 마이페이지(회원정보 수정 | 내 장소 | 여행지침서 | 맛집메이트 이력)
  * 카테고리별 지도
  * 맛집메이트 매칭
  * 실시간 채팅
  * ChatGPT를 이용한 여행일정 추천
+ 배포 후 시연 영상 : https://drive.google.com/file/d/1irbAiOe6oQmNDK0ax_sR8xburC2s6LaT/view?usp=sharing
+ 서비스 메뉴얼 : https://drive.google.com/file/d/1TmDazD4RB8IArrc7TBu1N4qli6z2ioRQ/view?usp=sharing


# ⚙ 개발 환경


+ Languages: Java, JS, HTML5, CSS
+ Framework: React, SpringBoot, JPA
+ Library: WebSocket, Stomp, SockJS(실시간 채팅), Axios
+ API: Kakao Login API, Kakao Map API, OpenAI API
+ Server: RabbitMQ(메세지 브로커 서버), AWS(EC2, Route 53, ElastiCache, ELB, ACM), Docker, Jenkins(배포용 서버), Nginx(웹 서버)
+ DB: Oracle DB, Redis(세션 클러스터링)
+ Platform: GitHub, Notion, Jira, Microsoft Teams
+ Tools: ERMaster(Eclipse), IntelliJ, VSCode, SQL Developer


# 🛠 시스템 구조


+ 데이터베이스 구조
<div align="center">
  <img src="https://github.com/fourroro/nolleogasil_backend/blob/chon/README_images/DB_Schema.png" alt="시스템 설계도" width="800px" />
</div><p/>


+ 시스템 설계도
<img src="https://github.com/fourroro/nolleogasil_backend/blob/chon/README_images/system_architecture.jpg" alt="시스템 설계도" width="1000px" />


# 🐧 팀 소개


<table>
  <tbody>
    <tr>
      <td align="center"><a href=""><img src="https://github.com/fourroro/nolleogasil_backend/blob/chon/README_images/chon.jpg" width="200px;" alt="chon"/><br /><sub><b>팀장 : 박초은</b></sub></a><br /></td>
      <td align="center"><a href=""><img src="https://github.com/fourroro/nolleogasil_backend/blob/sunmin/README_images/sunmin.jpg" width="200px;" alt="sunmin"/><br /><sub><b>팀원 : 전선민</b></sub></a><br /></td>
      <td align="center"><a href=""><img src="https://github.com/user-attachments/assets/aa9dff04-0a9d-42d6-b5de-5b02f2ea07ba" width="200px;" alt="hyouzl"/><br /><sub><b>팀원 : 홍유리</b></sub></a><br /></td>
      <td align="center"><a href=""><img src="README_images/minj.jpeg" width="200px;" alt="minj"/><br /><sub><b>팀원 : 장민정</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>


# 📌 역할 분담
### 👩‍💻공동작업
+ 기획, DB스키마 설계, 세션 클러스터링, 접근제한 설정, 서비스 배포의 모든 과정


### 🐸박초은
+ UI
  * 페이지: 메인화면, 카테고리별 지도(맛집, 카페, 숙소, 관광지), 위시리스트(내 장소), 맛집메이트 모집, 맛집메이트 상세정보, 보낸 신청 목록, 받은 신청 목록, 맛집메이트 이력
  * 공통 컴포넌트: top, footer(underBar), image 버튼
+ 기능
  * 현재 위치나 원하는 위치 주변의 카테고리별 장소 검색
  * 위시리스트에 추가&삭제, 위시리스트 조회, 위시리스트 카테고리별 필터링&정렬(기본순, 최신순, 오래된순)
  * 맛집메이트 공고 조회&정렬(날짜순, 거리순), 맛집메이트 신청&신청 취소 | 신청 수락&거절 | 신청이력 삭제, 맛집메이트 이력 조회
  * 맛집메이트 멤버 삭제(게시자만), 맛집메이트 멤버들에게 온도 부여


### 🐰 전선민
+ UI
  * 페이지 : 여행 일정 폼 작성 페이지, 추천 여행 일정표(ChatGpt API 응답 결과 화면), 내 여행 지침서(여행일정 목록)
  * 공통 컴포넌트 : checkbox tiles, date range picker, 날짜별 카드, 폴더 컴포넌트
+ 기능
   * 목적지 검색 자동 완성, ChatGpt API 연동 및 해당 API를 통한 사용자 맞춤 여행일정 추천
   * 여행일정 저장, 저장한 여행일정 상세 확인
   * 여행일정 목록 조회&정렬(최신순, 오래된순, 이름순), 여행일정 내용 수정 및 삭제


### 🍅 유리
+ UI
  * 페이지 : 채팅방 목록, 채팅방, 맛집메이트 공고글 작성 페이지
  * 공통 컴포넌트 : 채팅 서버
+ 기능
  * 맛집 메이트 공고글 작성
  * 채팅방 목록 조회(기본순, 최신순, 오래된순)
  * 실시간 채팅 기능


### 🥑장민정
+ UI
  * 페이지 : 회원가입, Kakao/Email 로그인, 마이페이지, 회원 정보 수정
  * 공통 컴포넌트 : 세션 유효성 검증
+ 기능
  * Kakao ID를 이용한 회원가입 및 로그인 + 테스트용 이메일 로그인
  * 회원 정보 조회 및 수정 기능, 회원 탈퇴 기능
  * 회원 가입 유효성 및 중복 검사, 로그인 유효성 및 중복 검사, 세션 유효성 검증


# 🖼 이미지 출처
<details>
 <summary>프로젝트 내 사용</summary>
 <a href="https://www.flaticon.com/free-icons/restaurant" title="restaurant icons">Restaurant icons created by Eucalyp - Flaticon</a><br/>
 <a href="https://www.flaticon.com/free-icons/cafe" title="cafe icons">Cafe icons created by Freepik - Flaticon</a><br/>
 <a href="https://www.flaticon.com/free-icons/villa" title="villa icons">Villa icons created by Freepik - Flaticon</a><br/>
 <a href="https://www.flaticon.com/free-icons/signs" title="signs icons">Signs icons created by Freepik - Flaticon</a><br/>
 <a href="https://www.flaticon.com/free-icons/eat" title="eat icons">Eat icons created by Freepik - Flaticon</a><br/>
 <a href="https://www.flaticon.com/free-icons/taxi" title="taxi icons">Taxi icons created by surang - Flaticon</a><br/>
 https://dribbble.com/shots/5386830-?utm_source=pinterest&utm_campaign=pinterest_shot&utm_content=%E2%97%8F%E2%97%8F%E2%97%8F%E2%97%8F&utm_medium=Social_Share<br/>
 <a href="https://www.flaticon.com/free-icons/map" title="map icons">Map icons created by Freepik - Flaticon</a><br/>
 <a target="_blank" href="https://icons8.com/icon/3907/temperature">Temperature</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>


 [여행일정]

 
 <a href="https://www.flaticon.com/kr/free-icons/" title="관광객 아이콘">관광객 아이콘  제작자: Freepik - Flaticon</a>	

 
 <a href="https://www.flaticon.com/kr/free-icons/" title="여행 아이콘">여행 아이콘 제작자: wanicon - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="가족 아이콘">가족 아이콘 제작자: wanicon - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="여행 아이콘">여행 아이콘 제작자: surang - Flaticon</a> 
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="바다 아이콘">바다 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="산 아이콘">산 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="부동산 아이콘">부동산 아이콘 제작자: Eucalyp - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/-" title="갑판 의자 아이콘">갑판 의자 아이콘  제작자: Smashicons - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="여행 아이콘">여행 아이콘 제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="래프팅 아이콘">래프팅 아이콘  제작자: Viktor Turchyn - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="여행 아이콘">여행 아이콘  제작자: SatawatDesign - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="식사 아이콘">식사 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="트레킹 아이콘">트레킹 아이콘 제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="쌀 아이콘">쌀 아이콘  제작자: xnimrodx - Flaticon</a> 
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="떡 아이콘">떡 아이콘  제작자: Paul Kovaloff - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="라면 아이콘">라면 아이콘  제작자: Vichanon Chaimsuk - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="떡갈비 아이콘">떡갈비 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="일본 아이콘">일본 아이콘 제작자: tulpahn - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/-" title="샤오 롱 바오 아이콘">샤오 롱 바오 아이콘 제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="피자 아이콘">피자 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="버거 아이콘">버거 아이콘  제작자: Smashicons - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title=" 아이콘"> 아이콘 제작자: surang - Flaticon</a>  
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title=" 아이콘"> 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="게 아이콘">게 아이콘 제작자: surang - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="회 아이콘">회 아이콘 제작자: Linector - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="곱창 아이콘">곱창 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/-" title="음식과 식당 아이콘">음식과 식당 아이콘  제작자: murmur - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="소고기 아이콘">소고기 아이콘 제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="치킨 아이콘">치킨 아이콘  제작자: Freepik - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/ui" title="ui 아이콘">Ui 아이콘 제작자: bsd - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/-" title="지도 및 위치 아이콘">지도 및 위치 아이콘 제작자: Taimoor D - Flaticon</a>
 
 
 <a href="https://www.flaticon.com/kr/free-icons/" title="휴지통 아이콘">휴지통 아이콘 제작자: iconading - Flaticon</a>
 
</details>
