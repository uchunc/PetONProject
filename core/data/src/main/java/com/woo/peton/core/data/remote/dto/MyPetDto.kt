package com.woo.peton.core.data.remote.dto

import com.woo.peton.domain.model.MyPet

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

fun MyPetDto.toDomain() = MyPet(
    id = id,
    ownerId = ownerId,
    name = name,
    gender = gender,
    breed = breed,
    birthDate = birthDate,
    neutered = neutered,
    registrationNumber = registrationNumber,
    content = content,
    imageUrl = imageUrl
)

fun MyPet.toDto() = MyPetDto(
    id = id,
    ownerId = ownerId,
    name = name,
    gender = gender,
    breed = breed,
    birthDate = birthDate,
    neutered = neutered,
    registrationNumber = registrationNumber,
    content = content,
    imageUrl = imageUrl
)