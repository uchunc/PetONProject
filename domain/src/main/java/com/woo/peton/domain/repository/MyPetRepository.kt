package com.woo.peton.domain.repository

import com.woo.peton.domain.model.MyPet
import kotlinx.coroutines.flow.Flow


interface MyPetRepository {
    fun getAllMyPets(): Flow<List<MyPet>>

    suspend fun getPetById(petId: String): MyPet?

    suspend fun savePet(myPet: MyPet): Result<Unit>

    // TODO 삭제기능
}