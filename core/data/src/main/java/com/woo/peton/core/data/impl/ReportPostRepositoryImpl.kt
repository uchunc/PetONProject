package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.ReportDataSource
import com.woo.peton.core.data.mapper.toDomain
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.ReportPostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReportPostRepositoryImpl @Inject constructor(
    private val dataSource: ReportDataSource
) : ReportPostRepository {
    override fun getPosts(type: ReportType): Flow<List<MissingPet>> {
        return dataSource.getPostsFlow(type.name).map { dtoList ->
            dtoList.map { dto -> dto.toDomain() }
        }
    }

    override fun getPostDetail(id: String): Flow<MissingPet?> {
        return dataSource.getPostDetail(id).map { dto ->
            dto?.toDomain()
        }
    }

    override suspend fun addPost(pet: MissingPet): Result<Boolean> {
        return try {
            dataSource.addPost(pet.toDto())
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}