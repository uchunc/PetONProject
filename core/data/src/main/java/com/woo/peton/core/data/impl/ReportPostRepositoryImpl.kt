package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.ImageDataSource
import com.woo.peton.core.data.datasource.ReportDataSource
import com.woo.peton.core.data.mapper.toDomain
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.ReportPostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class ReportPostRepositoryImpl @Inject constructor(
    private val dataSource: ReportDataSource,
    private val imageDataSource: ImageDataSource
) : ReportPostRepository {
    override fun getPosts(type: ReportType): Flow<List<ReportPost>> =
        dataSource.getPostsFlow(type.name).map { dtoList ->
            dtoList.map { dto -> dto.toDomain() }
        }

    override fun getPostDetail(id: String): Flow<ReportPost?> =
        dataSource.getPostDetail(id).map { it?.toDomain() }

    override suspend fun addPost(pet: ReportPost): Result<Boolean> = runCatching {
        val finalImageUrl = pet.imageUrl
            .takeIf { it.isNotBlank() && !it.startsWith("http") }
            ?.let { uri ->
                val fileName = "${UUID.randomUUID()}.jpg"
                imageDataSource.uploadImage(uri, "posts/$fileName").getOrThrow()
            } ?: pet.imageUrl
        dataSource.addPost(pet.copy(imageUrl = finalImageUrl).toDto())
        true
    }
}