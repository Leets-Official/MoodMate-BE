# MoodMate - 무드, 나이, 학과를 반영한 대학생 맞춤 1대1 매칭 서비스

# MoodMate <a href="https://www.moodmate.site"><img src="https://github.com/user-attachments/assets/18ad2a4a-07b4-45ab-98a1-d2b38a2eefc2" align="left" width="100"></a>
<a href="https://hits.seeyoufarm.com">
  <img src="https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FLeets-Official%2FMoodMate-BE&count_bg=%23333333&title_bg=%23FC4F59&icon=&icon_color=%23FC4F59&title=hits&edge_flat=false)](https://hits.seeyoufarm.com"/>
</a>

<br></br>

**📱 MoodMate |** [~~사이트 바로가기~~](https://www.moodmate.site/) (서비스 종료)
</br>
**📌 Official Account |** [MoodMate Instagram](https://www.instagram.com/be_at_beat?igsh=MTJmank3N3phZHYzeA==) </br>


<br>

### 🗓️ 8개월간의 기획, 개발, 출시 및 유지보수 (2023.10 ~ 2024.06)
### 💏 '가천대학교 대학생'을 대상으로 시즌 1에서 약 400명, 시즌 2에서 약 350명의 학생 사용자 유치

<div align="center">
  <h1><img src="https://github.com/Leets-Official/MoodMate-FE/raw/develop/public/illustration/common/logo/pinklogo.png"/></h1> 
</div>

<div align="center">
  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/develop/public/illustration/common/chat/chatlist.png"/>
</div>
<br />

MoodMate(무드메이트)는 **'가천대학교 대학생'** 을 대상으로 매일 밤 10시에 사용자의 **'데이트 무드', '나이', '학과'를 고려하여 남녀를 1대1로 매칭**해주는 서비스입니다. 

매칭된 남녀는 **채팅방에서 1:1 대화**를 나눌 수 있으며, 22시간 동안 유지되어 다음 날 오후 8시에 **채팅방이 비활성화** 되어 더 이상 대화를 나눌 수 없게 됩니다.

상대방과 더 이야기를 나누고 싶다면, 채팅방이 비활성화되기 전에 연락처나 SNS를 서로 주고 받으세요!

## 📄 목차

- [✍🏻 프로젝트 개요](#-프로젝트-기획-개요)
- [1️⃣ 시즌 1](#1-시즌-1)
   - [✨ 주요 기능 소개](#-주요-기능-소개)
- [2️⃣ 시즌 2](#2-시즌-2)
  - [🧐 시즌 1 유저 리서치 결과](#-시즌-1-유저-리서치-결과)
  - [🌊 주요 변경 사항](#-주요-변경-사항)
- [📊 서비스 배포 결과](#-서비스-배포-결과)
  - [🎤 홍보 과정](#-홍보-과정)
  - [🙋‍ 유저 수](#-유저-수)
  - [💬 매칭을 통해 생성된 채팅방 수](#-매칭을-통해-생성된-채팅방-수)
- [🧑🏻‍💻 BE 팀원 소개](#-be-팀원-소개)
- [🚀 핵심 기능](#-핵심-기능)
- [⚙️ 기술 스택](#-기술-스택)
- [🔨 시스템 아키텍처](#-시스템-아키텍처)
- [👥 Contributors](#-contributors)

<br />

## ✍🏻 프로젝트 기획 개요

- 기존 데이팅 서비스는 **외모 중심의 획일적인 매칭**에 집중하여, **진정한 소울메이트를 찾는** 사용자들의 갈증을 해소하지 못했습니다.
- 저희는 단순한 외모가 아닌, **깊이 있는 교감과 공통의 가치관을 통해 오래도록 함께할 수 있는 인연**을 원하는 사용자들의 목소리에 집중했습니다.
- 이러한 사용자들의 요구를 반영하여, **'무드'를 매개로 서로의 내적인 면을 먼저 이해하고 대화를 이어나갈 수 있는 매칭 서비스** '**MoodMate**'를 기획하게 되었습니다.

<br /><br/>

## 1️⃣ 시즌 1

### ✨ 주요 기능 소개
> 로그인 페이지

<p align="center">  <img src="https://github.com/user-attachments/assets/2c4b58e7-4cd8-45ff-803b-ff589f4bd624" align="center" width="32%"></p>

- URL로 서비스 도메인을 입력하게 되면 **로그인 페이지로 이동**합니다.
- **소셜 로그인**을 통해 로그인 과정이 진행됩니다.

<br /><br/>

> 회원 정보 입력 

<p align="center">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/정보1.png" align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/정보2.png"align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/정보3.png"align="center" width="32%"></p>
<p align="center">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/정보4.png" align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/정보5.png"align="center" width="32%"></p>

- 사용자는 **소셜 로그인 후 회원 정보를 입력**하게 됩니다.
- 입력 정보에는 **닉네임, 성별, 나이, 학과, 키워드**가 포함되며 **키워드를 제외한 나머지 정보를 이용하여 매칭**이 진행됩니다.

<br /><br/>

> 상대 무디 조건 설정

<p align="center">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/무디1.png" align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/무디2.png"align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/무디3.png"align="center" width="32%"></p>

- 사용자는 회원 정보 입력을 완료한 후, **상대방에 대한 조건**을 설정하게 됩니다.
- 설정 조건에는 **나이 구간, 같은 학과 선호 여부, 선호하는 무드**가 포함되며, 이 정보들은 **매칭 프로세스에서 중요한 기준으로 활용**됩니다.


<br /><br/>

> 메인페이지
<p align="center">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/메인1.png" align="center" width="32%"> <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/메인2.png"align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/비활메인.png"align="center" width="32%"><figcaption align="center">
  
<p align="center">매칭대기중 | 매칭 후 채팅중 | 비활성화중 메인</p>

- 회원 정보 입력과, 상대 무디 조건 설정이 완료되면 **메인페이지로 이동**합니다.
- 메인 페이지에서는 **매일 밤 10시에 시작되는 매칭 이벤트를 강조하는 카운트다운 타이머가 표시**됩니다. **카운트다운이 종료되면 자동으로 매칭 프로세스가 시작되며, 매칭된 사용자는 분홍색 배경의 페이지로 전환**되어 채팅방으로 들어가면 매칭된 사람과 대화를 할 수 있습니다.
- 하단의 탭 바를 통해 사용자는 다음과 같은 기능에 쉽게 접근할 수 있습니다
  - **채팅방 아이콘**: 매칭된 상대와의 **대화를 시작하거나 진행 중인 대화를 계속할 수 있으며,** **매칭이 되지 않은 사람은 채팅방에 입장이 불가능**합니다.
  - **마이페이지 아이콘**: 사용자의 프로필을 관리하고, 설정을 조정할 수 있는 **마이 페이지로 이동**합니다.
  - **비활성화 아이콘**: 사용자가 원할 경우 매칭 **이벤트 참여를 일시적으로 비활성화**할 수 있으며, **재활성화하기 전까지 매칭에서 제외**됩니다.

<br /><br/>

> 채팅

<p align="center">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/채팅1.png" align="center" width="32%"> <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/채팅2.png"align="center" width="32%">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/상대방채팅.png"align="center" width="32%">

<p align="center">채팅 미리보기 | 채팅중 | 상대 무디 정보 조회</p>

- 매칭이 되게 되어 채팅 페이지에 입장하게 되면, **상대로 부터 온 메시지를 미리보기로 확인할 수 있습니다.**
- **미리보기 바를 누르면 채팅방에 입장할 수 있으며**, **매칭된 상대와 1:1로 대화**를 나눌 수 있습니다.
- **상대의 아이콘을 눌러서 상대 무디의 정보도 확인할 수 있습니다.**

<br /><br/>

> 마이페이지

<p align="center">  <img src="https://github.com/Leets-Official/MoodMate-FE/raw/main/public/readme/마이페이지.png" align="center" width="32%"></p>

- 마이 페이지에서는 앞서 설정했던 **본인의 정보를 확인**할 수 있습니다.

## 2️⃣ 시즌 2
### 🧐 시즌 1 유저 리서치 결과

#### 리서치 홍보
<p align="center">  <img src="https://github.com/user-attachments/assets/e11b5ba1-3b8d-4169-93e9-2bee5aa3e2f9" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/fc895239-a3e2-4b2d-bfca-53f702d67ea2"align="center" width="32%"></p>

- 무드메이트 시즌1을 마무리 한 후, 웹사이트를 직접 사용한 학우분들의 의견을 듣고 사용자의 니즈를 파악하고 기능을 기획하기 위해 유저 리서치를 위한 설문조사를 실시하였습니다.
- 교내 커뮤니티 에브리타임에 글을 게시하여 총 7명의 학우분들의 답변을 받을 수 있었습니다.
- [구글폼 링크](https://docs.google.com/spreadsheets/d/1CelnL_2IOa0NW6McUnIA3LDejoJRQTSpjXFkpHqtK6I/edit?gid=1214227825#gid=1214227825)

#### 리서치 분석

<p align="center">  <img src="https://github.com/user-attachments/assets/708edfea-3e80-499b-ba79-4c861f85636d" align="center" width="70%"></p>

- 리서치에 참여한 인원은 **총 7명으로 남자 5명, 여자 2명**이었습니다.
- 비록 적은 인원이었지만, 지인들의 피드백과 교내 커뮤니티의 반응을 함께 참고하여 분석을 진행하였습니다.

<br>

<p align="center">  <img src="https://github.com/user-attachments/assets/4c0a9993-23e5-43d6-86ea-b2331c6c93cf" width="70%"></p> 

- '**추가로 필요한 서비스**'에 대한 의견 남성 응답자의 경우 다양한 항목이 제시되었으며, 특히 ‘**채팅 알림’과 ‘상대방 조건 추가**’가 높은 빈도로 언급되었습니다. 여성 응답자는 **‘채팅 알림’이 높은 빈도**로 언급되었습니다.

<p align="center">  <img src="https://github.com/user-attachments/assets/3beb554d-b355-473f-8513-984643b659bd" align="center" width="70%"></p> 

- '**서비스 개선이 필요하다고 느낀 부분**'에 대해서 총 다섯 분께서 의견을 주셨고,'**채팅 알림 도입**'을 가장 많이 제안해 주셨습니다.

#### 시즌 2 방향성 설계

> 채팅 알림 기능 우선 도입
- ‘채팅 알림 도입’이 남성 및 여성 응답자 모두에게서 높은 빈도로 언급되었습니다. 
- 이는 사용자들이 실시간으로 소통할 수 있는 환경을 필요로 하고 있음을 시사한다고 생각했습니다.
- 따라서 **FCM을 활용한 채팅 알림 기능**을 우선적으로 도입하여 사용자 경험을 개선하는 방향으로 계획을 수립했습니다.

> 상대방 조건 추가
- 남성 응답자들 사이에서 ‘상대방 조건 추가(키, 관심사, 취미 등)’에 대한 요구가 다수 제기되었습니다.
- 하지만 키를 상대방 조건으로 등록하게 되면 서비스 취지인 ' 내적인 면을 먼저 이해하고 대화를 이어나갈 수 있는 매칭 서비스'에 적합하지 않다고 생각했습니다.
- 따라서 **나를 나타낼 수 있는 키워드 개수를 확대하고, 기존 카테고리를 취미 관련 키워드로 전환**하여 사용자 경험을 개선하는 방향으로 계획을 수립했습니다. 

> 서비스 성능 및 안정성 개선
- 로딩 속도, 오류 개선과 같은 기능적 문제가 지적되었습니다.
- 기존에 개발진도 이러한 문제를 인지하고 있었기 때문에, **안정성과 성능 최적화에 주력**하여 사용자 경험을 개선하는 방향으로 계획을 수립했습니다.

> 지속적인 사용자 피드백 수집 및 반영
- 적은 인원이었지만 다양한 의견이 제공되었으며, 이는 서비스 개선 방향성에 중요한 자료가 되었습니다.
- **보다 빠르게, 보다 많은 유저**의 의견을 받기 위해 '에브리타임' 커뮤니티에서 유저와 실시간으로 소통하며 지속적으로 피드백을 받아 서비스를 개선하는 방향으로 계획을 수립했습니다. 

<br>

### 🌊 주요 변경 사항
> PWA 도입
<p align="center">  <img src="https://github.com/user-attachments/assets/d492e8dd-09fe-4ac1-85a5-5abb0cece2b7" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/dc89ba1a-910a-4ec1-b87f-765bafe6774f" align="center" width="32%"></p>

<p align="center">온보딩 페이지</p>

- 앱 알림 서비스와 UX, 채팅 기능을 개선하기 위해 **프로그레시브 웹 어플리케이션(PWA)을 도입**하였습니다.
- 유저 서비스 만족도 조사 결과, **채팅 알림 기능에 대한 유저의 요구가 확인**되었고, 이를 **Firebase의 FCM을 활용한 웹 푸쉬 기능을 적용**하여 사용자 경험을 개선하였습니다.
- Android, iOS, Desktop 앱으로 다운로드할 수 있습니다.

> 구글 로그인 → 카카오 로그인
<p align="center">  <img src="https://github.com/user-attachments/assets/95d60c12-0ba6-4c04-88a3-af1af17a7352" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/1d4371b2-cf45-46a5-a951-4ac84596a5ac" align="center" width="32%"></p>

<p align="center">구글 로그인(시즌1) | 카카오 로그인(시즌2)</p>

- **시즌 1에서는 구글 로그인 기능을 도입하여 서비스를 운영**하였습니다.
- 그러나, **일부 유저가 여러 개의 구글 계정을 보유한 경우, 하나의 유저가 여러 계정으로 가입하는 문제**를 완전히 방지할 수 없다는 한계가 있었습니다.
- 때문에, **시즌 2에서는 카카오 로그인으로 변경하여 중복 가입 문제를 막을 수 있었습니다.**

> 회원 탈퇴 및 닉네임 수정 API 도입
<p align="center">  <img src="https://github.com/user-attachments/assets/5d224e71-d00f-46d8-ab7b-022f5e710164" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/7904b217-8985-46a8-b452-e7be82e22652" align="center" width="32%"></p>
<p align="center">  <img src="https://github.com/user-attachments/assets/24c52d31-054f-419e-9f62-27e958c6b00b" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/da0542ac-c2af-48ff-832c-e107880db1eb" align="center" width="32%"></p>
<p align="center">  <img src="https://github.com/user-attachments/assets/96d9865c-373d-45dc-9420-ff2f7d440e02" align="center" width="32%">  </p>

<p align="center">회원 탈퇴 및 닉네임 수정 기능 공지</p>

**회원 탈퇴**
- 시즌 1에서는 회원 탈퇴 기능 없이 배포했으며, 관련 피드백이 없었기 때문에 시즌 2에서도 회원 탈퇴 기능을 포함하지 않고 배포했습니다. 
- 그러나 시즌 2에서는 회원 탈퇴 기능을 문의하는 유저가 많아졌습니다.
- 이에 따라 회원 탈퇴 기능의 필요성을 인지하고, 신속하게 탈퇴 회원 API를 제작하여 배포했습니다. 

**닉네임 수정**

- 시즌 1에서도 실수로 본인의 이름으로 가입하여 닉네임 수정을 원하는 유저가 많았습니다.
- 그러나 매칭 로직을 변경해야 했기 때문에, 짧은 서비스 기간 내에 이를 수정하고 닉네임 수정 API를 제작하는 데 어려움이 있었습니다.
- 이에 따라 시즌 2에서는 매칭 로직을 변경하고, 중복 체크 기능을 포함한 닉네임 수정 API를 제작하여 배포했습니다

> 유령 회원 방지
<p align="center">  <img src="https://github.com/user-attachments/assets/4353a8eb-7fca-442d-b5e0-acbb4902b57b" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/ed181fe3-bcb6-4079-821d-c2a72588ec80" align="center" width="32%"></p>
<p align="center">  <img src="https://github.com/user-attachments/assets/01924f34-0dad-4cf4-8e42-0318ad002a70" align="center" width="64%"></p>

- 시즌 1에서는 **매칭 비활성화 버튼을 누르지 않은 채 서비스를 이용하지 않는 유령 회원들이 매칭에 참여**하게 되어, 대화가 원활하게 이어지지 않는 문제가 있었습니다.
- 이를 해결하기 위해 **스프링 스케줄러(cron)를 활용**하여, 매칭 시점부터 다음 날 채팅방 비활성화 시간까지 채팅을 입력하지 않는 **유령 회원을 자동으로 매칭에서 제외**하도록 했습니다. 

> 무드 초점 전환(데이트 무드 -> 연애 무드)
<p align="center"><img src="https://github.com/user-attachments/assets/cba96dcd-3e6b-4430-bbcb-e2969c3b5739" align="center" width="29%">  <img src="https://github.com/user-attachments/assets/984946e5-0af7-4911-8db2-0c1ec699adb2" align="center" width="32%"></p>

<p align="center">데이트 무드(시즌1) | 연애 무드(시즌2)</p>

<p align="center"> <img src="https://github.com/user-attachments/assets/f27cac4a-72d7-4525-ab1b-829a7b6884b0" align="center" width="32%"> </p> 

<p align="center">무드 변경 공지</p>

**무드 초점 전환**
- 시즌 1에서는 “**어떤 무드의 데이트를 하고 싶은지**”에 초점을 맞춰 4개의 데이트 무드로 카테고리를 나누어 진행했습니다.
- 시즌 1 회고에서, 데이트 무드로 나누는 것이 데이트의 일회성 활동에 중점을 두는 것 같다는 의견이 제기되었습니다.
- 이에 따라 내부 회의에서 시즌 2에서는 “**어떤 연애를 하고 싶은지**“로 초점을 전환하고, **연애 스타일에 맞춰 카테고리를 재구성**하여 사용자가 더 깊이 있는 관계 형성을 목표로 할 수 있도록 개선하기로 결정하였고, 시즌 2에 이를 반영하여 배포하였습니다.

**무드 변경(뜨거운 -> 잔잔한)**

- 연애 카테고리(뜨거운, 편안한, 설레는, 재밌는)를 나누어 배포했을 때, “뜨거운” 무드에 대한 유저의 선호도가 매우 낮다는 것을 확인했습니다.
- 이에 따라 효율적인 서비스 운영을 위해 카테고리 조정이 필요하다고 판단하였고, 내부 회의를 통해 “뜨거운” 무드를 “잔잔한” 무드로 변경했습니다.
- 그 결과, 잔잔한 무드를 선택하는 사용자가 점차 늘어나면서 사용자들이 고르게 분산되었고, 서비스 효율성이 향상되었습니다.

> 키워드 개수 확대 및 취미 관련 카테고리로 변경
<p align="center">  <img src="https://github.com/user-attachments/assets/5c5c02a1-f811-43e6-ba4c-c8dfe978c1e0" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/0f65afc1-c57e-4d6f-b9e8-2ce1a2489525" align="center" width="31.17%"></p>

<p align="center">키워드(시즌1) | 키워드(시즌2)</p>

<p align="center">  <img src="https://github.com/user-attachments/assets/f27cac4a-72d7-4525-ab1b-829a7b6884b0" align="center" width="32%"> </p> 

<p align="center">키워드 추가 공지</p>

- 유저 서비스 만족도 조사 결과, 상대방 조건 추가(키, 관심사, 취미 등)에 대한 유저의 요구가 확인되었으며, 이를 반영하여 **기존 카테고리를 취미 관련 키워드로 전환하고, 키워드 개수를 확대**하여 사용자 경험을 개선하였습니다
- **취미 카테고리로 변경**함으로써 **상대방의 취미를 이전보다 더 자세히 알 수 있었다**는 긍정적인 피드백을 받았습니다.
- 또한, **서비스 도중 유저로부터 받은 추천 키워드(“워커홀릭”, “집순돌이”, “춤”, “애주가”)를 추가**하여 사용자들이 자신을 표현할 수 있는 폭을 더욱 넓혔습니다.

> 매칭 알고리즘 고도화

[✍🏻 매칭 알고리즘 고도화 Wiki 보러가기](https://github.com/Leets-Official/MoodMate-BE/wiki/Enhancing-the-Matching-Algorithm)

> 채팅 고도화

[✍🏻 채팅 고도화 Wiki 보러가기](https://github.com/Leets-Official/MoodMate-BE/wiki/Enhancing-the-Chatting)

> 인프라 최적화

[✍🏻 인프라 최적화 Wiki 보러가기](https://github.com/Leets-Official/MoodMate-BE/wiki/Infrastructure-Optimization)


## 📊 서비스 배포 결과

### 🎤 홍보 과정
#### 1. 교내 커뮤니티 사이트 '에브리타임'에 홍보게시물 게시

> 시즌 1
<p align="center">  <img src="https://github.com/user-attachments/assets/bd3ff758-270e-4409-965b-2a19bac45e11" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/c4eb0013-d9be-40de-a6fb-6352ca569265" align="center" width="32%"></p>

> 시즌 2
<p align="center">  <img src="https://github.com/user-attachments/assets/443f2d9f-fb5d-496f-a95c-04b7cae28b70" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/1b8edef7-2fcf-42b7-bee7-b2b07e4efe82" align="center" width="32%">  <img src="https://github.com/user-attachments/assets/44484e21-1dd4-43f3-9625-46dd8d679626" align="center" width="32%"></p>


#### 2. 공식 인스타그램 계정에 홍보글 게시
> 시즌 1
<p align="center">  <img src="https://github.com/user-attachments/assets/bba99e62-79b6-4f8a-8796-2467f113e5aa" align="center" width="32%"> </p>

> 시즌 2
<p align="center">  <img src="https://github.com/user-attachments/assets/e3acf93c-0eb7-4787-b889-be82d7f479bc" align="center" width="32%"> </p>


#### 3. 교내 홍보 포스터 부착
> 시즌 1
<p align="center">  <img src="https://github.com/user-attachments/assets/8acc0cf5-100f-4dc3-8f45-6608a9cde946" align="center" width="32%"> </p>

> 시즌 2
<p align="center">  <img src="https://github.com/user-attachments/assets/c1852b2a-028d-4471-ba94-2af1fc70a5c0" align="center" width="32%"> </p>

### 🙋‍ 유저 수
> 시즌 1
<p align="center">  <img src="https://github.com/user-attachments/assets/c68d6e72-8c45-4ab5-b836-35dee56e9e98" align="center" width="70%"> </p>

- 배포기간 (2023.12.18 - 2023.12.26 총 9일) 운영 DB 기준 **회원가입 수 총 396명**을 기록하였습니다.

> 시즌 2
<p align="center">  <img src="https://github.com/user-attachments/assets/26580355-0215-482d-9cfb-41cd406744db" align="center" width="70%"> </p>

- 배포기간 (2024.05.17 - 2023.06.01 총 16일) 운영 DB 기준 **회원가입 수 총 342명**을 기록하였습니다.
- 첫날 가입한 9명은 정식 출시 전에 QA를 위해 가입한 팀원들입니다.

### 💬 매칭을 통해 생성된 채팅방 수
> 시즌 1
<p align="center">  <img src="https://github.com/user-attachments/assets/48d8ad28-8a4c-4e1f-991d-5caf84936882" align="center" width="70%"> </p>

- 매칭 서비스 기간 (2023.12.19 - 2023.12.25, 총 7일) 동안 운영 DB 기준으로 **하루 평균 약 87개의 채팅방이 생성**되었으며, 이는 **하루 평균 약 174명의 남녀가 매칭**되었다는 의미입니다. 
- 시간이 지남에 따라 매칭된 남녀 수가 꾸준히 증가하면서 더 많은 채팅방이 생성되었습니다. 특히 2023년 12월 25일에는 **가장 많은 110개의 채팅방이 생성**되었으며, 이는 **총 220명의 남녀가 매칭**된 것을 의미합니다.

> 시즌 2
<p align="center">  <img src="https://github.com/user-attachments/assets/577a5345-3350-4e1d-8b36-414db60749ae" align="center" width="70%"> </p>

- 매칭 서비스 기간 (2024.05.18 - 2024.05.31, 총 14일) 동안 운영 DB 기준으로 **하루 평균 약 59개의 채팅방이 생성**되었으며, 이는 **하루 평균 약 118명의 남녀가 매칭**되었다는 의미입니다.
- **유령 회원을 비활성화**함으로써 실제로 서비스를 사용하는 사용자들 간의 매칭이 이루어져 **매칭의 질이 향상되었고, 실제 대화가 이루어질 가능성이 높힐 수 있었습니다.**
- 또한, **유령 회원을 제외했음에도 불구하고 매칭된 채팅방 수가 꾸준히 증가**하는 것은 **유효 사용자 기반이 지속적으로 확대되고 있음을 시사**하며, 이는 **서비스 개선 노력의 성공과 사용자 경험이 긍정적임을 의미**합니다.

## 🧑🏻‍💻 BE 팀원 소개

|                                                                                                                                      이동훈                                                                                                                                       |                                                               이서빈                                                                |                                                                                                                                 인지원                                                                                                                                  |                                                                                                                                     장민호                                                                                                                                      |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                                                                                <img src="https://avatars.githubusercontent.com/u/125895298?v=4" width="120" />                                                                                                 |                          <img src="https://avatars.githubusercontent.com/u/70849467?v=4" width="120" />                          |                                                                                            <img src="https://avatars.githubusercontent.com/u/108799865?v=4" width="120">                                                                                             |                                                                                                 <img src="https://avatars.githubusercontent.com/u/84257033?v=4" width="120">                                                                                                 |
|                                                                                                                                **BE Developer**                                                                                                                                |                                                              **BE Developer**                                                              |                                                                                                                                 **BE Developer**                                                                                                                                 |                                                                                                                                     **BE Developer**                                                                                                                                     |
| [<img src="https://img.shields.io/badge/GitHub-000000?style=flat&logo=github&logoColor=white"/>](https://github.com/hoonyworld) [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white"/>](https://www.linkedin.com/in/donghoon0203) | [<img src="https://img.shields.io/badge/GitHub-000000?style=flat&logo=github&logoColor=white"/>](https://github.com/leeseobin00) [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white"/>](https://www.linkedin.com/in/%EC%84%9C%EB%B9%88-%EC%9D%B4-436373288/) | [<img src="https://img.shields.io/badge/GitHub-000000?style=flat&logo=github&logoColor=white"/>](https://github.com/jiixon) [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white"/>](https://www.linkedin.com/in/jiwon-in-814a392a6) | [<img src="https://img.shields.io/badge/GitHub-000000?style=flat&logo=github&logoColor=white"/>](https://github.com/MinhoJJang) [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white"/>](www.linkedin.com/in/minho-jang-8765982b9) |

<br />

<br />

## 🚀 핵심 기능

### 매칭 시스템

> 유저가 설정한 데이트 무드, 나이 선호도, 같은 학과 선호 여부를 고려

- 기존 **Gale-Shapley 알고리즘을 서비스에 맞게 커스텀**하여, N명의 남성과 M명의 여성(또는 그 반대)의 **세 가지 선호도(무드, 나이, 같은 학과 여부)를 기반으로 최적의 매칭을 제공**하도록 개발하였습니다. 
- **매칭 활성화 시 하루에 한 번 자동으로 매칭**이 이루어지며, **매칭이 비활성화된 경우, 유저가 다시 활성화할 때까지 매칭이 진행되지 않도록 구현**했습니다.
- 시즌 1에서는 시스템이 **user_id를 테이블에 저장된 순서대로 가져와 이전에 매칭된 사용자가 다시 매칭되는 문제**가 있었습니다. 이를 해결하기 위해 매칭 **시스템의 무작위성을 높이고, 이미 매칭된 사람과 다시 만날 확률을 최소화하는 작업을 진행**했습니다.
- 시즌 2에서는 **시즌 1에서 보수하지 못했던, 매칭 과정에서 모든 사용자가 참여할 수 있도록 보장하고, 매칭되지 않는 문제를 해결하며, 예외 처리를 통해 시스템 안정성을 강화**하는 등 핵심적인 기술 개선 작업을 진행했습니다. 또한, **매칭 알고리즘 작동 중 회원 탈퇴를 방지**하여 프로그램의 신뢰성을 높였습니다.

### 1:1 실시간 채팅

> 웹 소켓과 Stomp 프로토콜을 이용한 1:1 채팅방 구현

- 실시간 채팅에 적합하며 양방향 통신을 지원하는 Socket통신 방식을 **WebSocket 프로토콜을 사용**하여 개발하였습니다.
- 효율적인 메세지전송을 위해 **서브프로토콜로 Stomp를 사용하여 메시지의 유형, 형식, 내용을 정의하여 규격을 갖춘 메시지를 전송할 수 있게 구현**했습니다.
- **구독 정보를 redis 서버에 ChannelTopic으로 저장해 같은 Topic을 구독하고 있는 사용자에게 메세지를 송수신하도록 구현**했습니다.
- 시즌 2에서는 **채팅 보관 DB를 RDBMS 에서 NoSQL인 MongoDB로 이전**하여 데이터 처리 성능 최적화를 진행했습니다.

<br />

## ⚙️ 기술 스택

#### Framework
<img src="https://img.shields.io/badge/Spring_Boot_3-0?style=flat-square&logo=spring-boot&logoColor=white&color=%236DB33F">   <img src="https://img.shields.io/badge/Gradle-0?style=flat-square&logo=gradle&logoColor=white&color=%2302303A">

#### ORM
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=Databricks&logoColor=white">

#### Authorization
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white">  <img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=flat-square&logo=JSON Web Tokens&logoColor=white">

#### Security
<img src="https://img.shields.io/badge/Fail2ban-00A98F?style=flat-square&logo=shield&logoColor=white" />

#### Database
<img src="https://img.shields.io/badge/MySQL-4479A1.svg?style=flat-square&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/MongoDB-0?style=flat-square&logo=mongodb&logoColor=white&color=%2347A248">

#### Cloud
<img src="https://img.shields.io/badge/Google%20Cloud-4285F4?style=flat-square&logo=google-cloud&logoColor=white" /> <img src ="https://img.shields.io/badge/AWS EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white">  <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white">

#### CI/CD
<img src="https://img.shields.io/badge/Jenkins-0?style=flat-square&logo=Jenkins&logoColor=white&color=%23D24939"> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white">

#### Monitoring
<img src="https://img.shields.io/badge/ElasticSearch-005571?style=flat-square&logo=elasticsearch&logoColor=white" alt="ElasticSearch">  <img src="https://img.shields.io/badge/Logstash-005571?style=flat-square&logo=logstash&logoColor=white" alt="Logstash">  <img src="https://img.shields.io/badge/Kibana-005571?style=flat-square&logo=kibana&logoColor=white" alt="Kibana">

#### Other
<img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=jira&logoColor=white" alt="Jira">  <img src="https://img.shields.io/badge/ Swagger-6DB33F?style=flat-square&logo=swagger&logoColor=white">

## 🔨 시스템 아키텍처
<img src="https://github.com/user-attachments/assets/bebd3901-3807-4b72-b00c-d4b1a82edd43">

## 👥 Contributors
- [MoodMate-FE Repository](https://github.com/Leets-Official/MoodMate-FE)
