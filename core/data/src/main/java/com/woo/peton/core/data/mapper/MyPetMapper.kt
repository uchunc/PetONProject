package com.woo.peton.core.data.mapper

import com.woo.peton.core.data.remote.dto.MyPetDto
import com.woo.peton.domain.model.MyPet

fun MyPetDto.toDomain(): MyPet {
    return MyPet(
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
}


fun MyPet.toDto(): MyPetDto {
    return MyPetDto(
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
}
