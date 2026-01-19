package com.woo.peton.core.data.datasource

import com.woo.peton.core.data.remote.dto.MyPetDto
import kotlinx.coroutines.flow.Flow

interface MyPetDataSource {
    fun getAllMyPets(): Flow<List<MyPetDto>>

    suspend fun getPetById(petId: String): MyPetDto?

    suspend fun savePet(petDto: MyPetDto)
}