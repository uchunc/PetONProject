package com.woo.peton.core.data.dto

import com.woo.peton.domain.model.User

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

// DTO -> Domain 변환
fun UserDto.toDomain() = User(
    uid = uid,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    profileImageUrl = profileImageUrl,
    fcmToken = fcmToken,
    latitude = latitude,
    longitude = longitude,
    geoHash = geoHash,
    address = address,
    interests = interests
)

// Domain -> DTO 변환
fun User.toDto() = UserDto(
    uid = uid,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    profileImageUrl = profileImageUrl,
    fcmToken = fcmToken,
    latitude = latitude,
    longitude = longitude,
    geoHash = geoHash,
    address = address,
    interests = interests
)