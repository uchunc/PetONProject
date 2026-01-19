package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.MyPetDataSource
import com.woo.peton.core.data.mapper.toDomain
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.repository.MyPetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyPetRepositoryImpl @Inject constructor(
    private val dataSource: MyPetDataSource
) : MyPetRepository {
    override fun getAllMyPets(): Flow<List<MyPet>> {
        return dataSource.getAllMyPets().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getPetById(petId: String): MyPet? {
        return try {
            dataSource.getPetById(petId)?.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun savePet(myPet: MyPet): Result<Unit> {
        return try {
            dataSource.savePet(myPet.toDto())
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}