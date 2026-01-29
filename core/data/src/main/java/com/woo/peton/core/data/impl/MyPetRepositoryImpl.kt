package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.ImageDataSource
import com.woo.peton.core.data.datasource.MyPetDataSource
import com.woo.peton.core.data.mapper.toDomain
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.repository.MyPetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MyPetRepositoryImpl @Inject constructor(
    private val dataSource: MyPetDataSource,
    private val imageDataSource: ImageDataSource
) : MyPetRepository {
    override fun getAllMyPets(): Flow<List<MyPet>> =
        dataSource.getAllMyPets().map { dtoList ->
            dtoList.map { it.toDomain() }
        }

    override suspend fun getPetById(petId: String): MyPet? =
        runCatching{ dataSource.getPetById(petId)?.toDomain() }.getOrNull()

    override suspend fun savePet(myPet: MyPet): Result<Unit> = runCatching{
        val finalImageUrl = myPet.imageUrl
            .takeIf { it.isNotBlank() && !it.startsWith("http") }
            ?.let { uriString ->
                val fileName = "${UUID.randomUUID()}.jpg"
                val path = "pets/$fileName"
                imageDataSource.uploadImage(uriString, path).getOrThrow()
            } ?: myPet.imageUrl
        val petToSave = myPet.copy(imageUrl = finalImageUrl)
        dataSource.savePet(petToSave.toDto())
    }
}