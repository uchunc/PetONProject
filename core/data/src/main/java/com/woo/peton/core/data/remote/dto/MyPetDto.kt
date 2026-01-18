package com.woo.peton.core.data.remote.dto

data class MyPetDto(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val gender: String = "",
    val breed: String = "",
    val birthDate: String = "",
    val neutered: Boolean = false,
    val registrationNumber: String = "",
    val content: String = "",
    val imageUrl: String = ""
)