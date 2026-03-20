# 🐾 Pet ON (펫츠온)

실종된 반려동물을 다 같이 찾는 가장 쉬운 방법 

## 📌 Project Overview

반려동물 실종 시 빠른 신고와 대응을 돕고, 전문적인 동물 탐정단을 연결하여 소중한 가족을 찾을 수 있도록 지원하는 안드로이드 애플리케이션입니다. 

## 🛠 Tech Stack

### Language & Framework

<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"> <img src="https://img.shields.io/badge/Android%20SDK%2036-3DDC84?style=for-the-badge&logo=android&logoColor=white"> <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=JetpackCompose&logoColor=white"> 

### Network & Backend

<img src="https://img.shields.io/badge/Firebase%20Auth-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white"> <img src="https://img.shields.io/badge/Firestore-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white"> <img src="https://img.shields.io/badge/Firebase%20Storage-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white"> 

### Architecture & Libraries

| 분류 | 기술 스택 |
| :--- | :--- |
| **Architecture** | MVVM Pattern, Clean Architecture |
| **DI (Dependency Injection)** | Hilt |
| **Asynchronous** | Coroutine, Coroutine Flow |
| **Maps & LBS** | Google Map, Google Fused Location |
| **Navigation** | Jetpack Navigation |

## 📱 Main Features

### 1. 홈 & 반려동물 프로필 

    나의 반려동물 관리: 이름, 성별, 품종(코숏 치즈 등), 나이 등 상세 정보 등록 

    즉시 신고 시스템: 홈 화면에서 유실 상황 발생 시 바로 신고 가능 

    최신 정보 업데이트: 지역에 상관없이 최신 등록된 신고글 실시간 확인 

### 2. 신고 및 제보 (실시간 지도) 

    실시간 지도 뷰: 지도 마커를 통해 실종/목격/임보 위치 한눈에 파악 

    상태 필터링: 실종, 목격, 임보 등 카테고리별 필터 기능 제공 

    상세 게시글: 실종 일시, 장소, 특징(흰 양말을 신음 등)을 포함한 상세 정보 작성 

## 🏗 Information Architecture (IA)

    홈: 빠른 신고, 최근 게시물, 보호소 동물, 전문 탐정단 

    신고/제보: 게시글 확인 및 작성, 댓글 제보 

    채팅: 탐정단 의뢰 채팅, 목격 제보 채팅 

    내 정보: 반려동물 프로필, 관심 동물, 신고/제보 기록 </details> 

## 🚀 Development Plan (Future)

    실시간 알림 서비스: 신고 게시글 등록 시 인근 사용자들에게 즉시 알림 발송 

    예상 경로 생성: 제보된 마지막 시간과 위치를 기반으로 반려동물의 이동 예상 경로 도출 

    전문 탐정단 서비스: 검증된 전문 탐정단 예약 및 1:1 채팅 의뢰 기능 고도화 

    SNS 연동: 펫츠온 공식 SNS 및 커뮤니티 게시물 자동 공유 요청 기능
