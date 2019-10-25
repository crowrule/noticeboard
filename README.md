# 개발 환경
 - Backend
	 - Java : 1.8
	 - Spring Boot 2.2.0
	 - H2-Databse
	 - Gradle 5.6.2
	 -
 - FrontEnd
	 - Node  : 10.16.3
	 - NPM : 6.9.0
	 - Vue : 2.6

# 설치

- Code URL : https://github.com/crowrule/noticeboard
- Backend
	- .Code Download 후 noticeboarder로 이동
	- 다음 명령 실행 
		1. gradlew clean booJar (Linux인 경우 ./gradlew clean bootJar
		2. cd execue
		3. java -jar noticeboard.jar
	- 확인 : 브라우져에서 Swagger UI 접속 : http://localhost:7000/swagger-ui.html
- Frontend
	- 위에서 아래 명령 진행 
		1. cd ..\..\frontend 실행
		2. dependency module download : npm install
		3. 실행(Demo 용) : npm run dev 
	- 확인 : 브라우져에서 localhost:8080/app.html 접속


# Rest Api
## Overview
- GET /api/notice : 공지사항들을 조회. Page 정보를 활용해 List의 특정 부분만 가져옴.
- GET /api/notice/{id} : 공지사항의 id값을 통해 1건의 공지 사항만을 가져옴.
- POST /api/notice : 신규 공지 사항을 등록. 
- PUT /api/notice/{id} : 기존 공지 사항을 갱신함.
- DELETE /api/noice/{id} : 특정 공지 삭제
- GET /login : 사용자 정보를 이용해서 jwt 를 가져옴.

각 Api에서 사용하는Request Parameter, Request Header, Request BodyResponse Type등은 Swagger Document를 참고. : http://localhost:7000/swagger-ui.html

## Return Code

- 200 : 요청 성공
- 204 : 공지 사항 조회 시 해당 공지 내용이 없을 경우
- 401
	- Login 시에 id/password가 맞지 않았을 때.
	- POST/PUT/DELETE 시에 X-AUTH-KEY header에 jwt값이 없거나, 등록된 사용자가 아닌 경우
- 403
	- PUT/DELETE 시에  요청한 사용자가 권한이 없을 경우
- 404
	- PUT/DELETE 시에 대상 공지 사항을 찾을 수 없는 경우
- 405
	- Request Parameter가 맞지 않았을 경우
- 500
	- Server Internal Error

## Custom HTTP Header 정보
- Page 정보 : GET /api/notice 시에 Response Header로 아래 정보 참조 필요
	- X-PAGE-SIZE : 1 page의 Size. 기본값은 5.
	- X-TOTAL-PAGE : 총 page 수.
	- X-TOTAL-COUNT : 총 Element 숫자
	- X-CURRENT-PAGE : 현재 Page Index (0부터 시작)
	- X-CURRENT-PAGE-SIZE : 현재 Page에 포함된 Element의 수. X-PAGE-SIZE를 넘지 않는다.
- Token 정보
	- X-AUTH-KEY : login 시에 Response로 받은 JWT 값. POST/PUT/DELETE Request 시에 Header에 포함되어야 함.

# User Management


 - Server 시작과 동시에 두 명의 User 추가 : user, admin
	 - user의 password : password
	 - admin의 password : admin
 - 별도의 User 등록/삭제 Process는 없음.
 - 내부적으로 USER와 ADMIN Role 두 개가 Setting 되었고, 각 user에게는 한 가지 Role이 부여됨,
	 
|USER ID         |ROLE              |  권한                                   |
|----------------|------------------|------------------------------------------|
|user| USER      |     공지 사항을 게시할 수 있고, 자기가 작성한 공지사항을 수정/삭제할 수 있음.            |
|admin|ADMIN | USER role이 가진 권한에, 타인이 작성한 공지사항도 삭제할 수 있음            |

# UI 
## Layout
- Login Header / 공지 사항 List / 세부 공지사항 3영역으로 구분
- Login 하지 않은 상태에서는 공지 사항 조회만 가능
- 공지 사항 List 중에 하나를 Click 하면 공지사항 내용/작성자/작성일/최종 수정일 을 확인할 수 있음.
- User가 Login 하면 신규 공지 사항 작성 및 수정이 가능하도록 버튼이 활성화 됨.
- Login 이후 Token을 저장하지 않기 때문에 F5로 화면 갱신 했을 때, Logout됨.
