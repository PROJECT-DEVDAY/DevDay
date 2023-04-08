# Dev Day 개발자 좋은 습관 만들기 챌린지 
![Devday](./image/Thumbnail.png)
#### 좋은 습관을 만들기 위한 챌린지 서비스

#### 기간
2023.02.20 ~ 2023.04.17

[목차]
1. 서비스 개요
2. 주요 기능 소개
3. 기술 스택
4. 아키텍처
5. ERD
6. 서비스 소개
7. 참여자
8. 포트포워딩 문서


## 서비스 개요

1일 1커밋, 1일 1알고리즘 등 인증과정을 자동화 챌린지와 사진인증 방식의 자유 챌린지를 통해 개발자들이 좋은 습관을 형성할 수 있도록 도와주는 챌린지 서비스입니다.


## 주요 기능 소개 

- 알고리즘 챌린지 : 백준 SOLVED.AC 데이터를 크롤링 후 DB 데이터와 비교하여 인증과정 자동화
- 커밋 챌린지 : Github 커밋 기록을 크롤링 후 DB 데이터와 비교하여 인증과정 자동화
- 인증서 발급 : 블록체인에 저장된 챌린지 인증 기록을 가지고 인증서 발급
- 결제/환불 : 토스 페이먼츠 API와 농협 오픈플랫폼 가상계좌를 사용하여 결제 시스템 구현
- MSA : Spring Cloud 기반으로 넷플릭스에서 개발한 Eureka Server를 사용하여 MSA 구조로 프로젝트 개발, FeignClient를 사용하여 마이크로 서비스 내부통신 구현
- Jenkins Pipeline : Docker와 Jenkins를 사용하여 CI-CD Pipeline 구축
- 스마트 컨트랙트 : Solidity 프로그래밍 언어를 사용하여 챌린지 기록 저장시 계약 자동 시행


## 기술 스택

<!-- ### FrontEnd
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Next.js](https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=Next.js&logoColor=white)            


### BackEnd     
![SpringBoot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)


###  Server & CI/CD
<img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">


### Communication
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![GoogleMeet](https://img.shields.io/badge/GoogleMeet-00897B?style=for-the-badge&logo=Google%20Meet&logoColor=white) -->
- **FrontEnd**
    - Visual Studio Code
    - Node.js : 9.4.1
    - react : 18.2.0
    - react-dom : 18.2.0
    - react-chartjs-2 : ^5.2.0
    - react-hook-form : ^7.43.8
    - redux : ^4.2.1
    - husky : ^8.0.3
    - eslint : ^8.2.0
    - prettier : 2.8.4
    - tailwindcss : ^3.2.7
    - axios : ^1.3.4
- **BackEnd**
    - IntelliJ
    - OpenJDK 11
    - Gradle : 7.6.1
    - SpringBoot v2.7.9
    - SpringCloud : 2021.0.6
        - Netflix Eureka Service (Discovery Service)
        - API Gateway
    - Spring Security
- **BlockChain** :
    - Metamask
    - Solidity
    - Web3
    - Remix IDE
    - Sepolia
- **CI/CD**
    - AWS EC2
    - Docker
        - Bridge Network
    - Jenkins
        - Pipeline
- **협업 툴**
    - Git Lab
    - Jira
    - Mattermost
    - Discord
    - Notion
- **DB**
    - MySQL
    - Redis


## 아키텍처
![Architecture](./image/아키텍처.png)

## ERD

### 유저 서비스 ERD
![UserERD](./image/유저.png)

### 챌린지 서비스 ERD
![ChallengeERD](./image/챌린지.png)

### 유저 서비스 ERD
![PayERD](./image/페이.png)

## 피그마
![Figma](./image/Devday.png)

## 서비스 소개 (화면)

### 메인페이지
![Main](./gif/메인페이지.gif)

### 회원가입
![Join](./gif/회원가입.gif)

### 로그인
![Login](./gif/로그인.gif)

### 로그인 필요
![NeedLogin](./gif/로그인이필요합니다.gif)

### 마이페이지
![Profile](./gif/프로필사진수정.gif)

### 마이페이지 수정
![UserInfo](./gif/유저정보변경.gif)

### 마이페이지 예치금
![Deposit](./gif/마이페이지예치금.gif)

### 마이페이지 상금
![Reward](./gif/상금조회.gif)

### 아이디 찾기
![FindId](./gif/아이디찾기.gif)

### 비밀번호 찾기
![FindPassword](./gif/비밀번호찾기.gif)

### 챌린지 참여하기
![JoinChallenge](./gif/챌린지 참여하기.gif)

### 참가챌린지 조회
![FindChallenge](./gif/참가챌린지조회.gif)

### 자신이 참여한 챌린지
![MyChallenge](./gif/자신이 참여한 챌린지.gif)

### 챌린지저장 과정
![Algo](./gif/챌린지저장과정+알고.gif)
![Commit](./gif/챌린지저장과정+커밋+자유주제.gif)

### 인증서 목록
![Certificate](./gif/인증서목록.gif)

## 참여자 - 역할


|                        박태환(팀장)                        |                       최형운                       |                     이동준                       |                     김기윤                       |                  신대득                       |                  홍금비                      |
| :----------------------------------------------------------: | :-------------------------------------------------------: | :-----------------------------------------------------: | :-----------------------------------------------------: | :-----------------------------------------------------: | :-----------------------------------------------------: |
| <img src="https://github.com/pthwan27.png" width="200"/> | <img src="https://github.com/choihyoingwoon.png" width="200"/> | <img src="https://github.com/Djunnni.png" width="200"/> | <img src="https://github.com/keeeeeey.png" width="200"/> |<img src="https://github.com/daydeuk.png" width="200"/> |<img src="https://github.com/GeumBi-Hong.png" width="200"/> |
|       [pthwan27](https://github.com/pthwan27)        |         [choihyoingwoon](https://github.com/choihyoingwoon)         |          [Djunnni](https://github.com/Djunnni)          |          [Keeeeeey](https://github.com/Keeeeeey)          |     [daydeuk](https://github.com/daydeuk)          |     [GeumBi-Hong](https://github.com/GeumBi-Hong)          |
|                         Web FrontEnd & CI-CD & UCC                          |                       Web FrontEnd & CI-CD & UI/UX                        |                       Web BackEnd & BackEnd & CI-CD                       |                      Web BackEnd & FrontEnd & CI-CD                       |                 Web BackEnd & CI-CD & Blockchain                       |             Web BackEnd & CI-CD & Blockchain                       |



## 포트 포워딩 문서 

1. Frontend
2. Backend
3. CI/CD
4. ERD

