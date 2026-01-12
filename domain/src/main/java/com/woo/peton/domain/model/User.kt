package com.woo.peton.domain.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",

    // 1. 마이페이지 UI용
    val profileImageUrl: String? = null,

    // 2. 알림 및 타겟팅용
    val fcmToken: String? = null, // Firebase Cloud Messaging 토큰

    // 3. 하이브리드 위치 시스템용 (내 근처 알림)
    val latitude: Double? = null,
    val longitude: Double? = null,
    val geoHash: String? = null, // 위치 검색 최적화를 위한 GeoHash
    val address: String? = null, // "서울시 강남구" 등 (UI 표시용)

    // 4. 필터링용
    val interests: List<String> = emptyList() // 예: ["DOG", "CAT", "MISSING"]
)