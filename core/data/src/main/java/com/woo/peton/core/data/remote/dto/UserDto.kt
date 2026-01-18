package com.woo.peton.core.data.remote.dto

data class UserDto(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String? = null,
    val fcmToken: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val geoHash: String? = null,
    val address: String? = null,
    val interests: List<String> = emptyList()
)