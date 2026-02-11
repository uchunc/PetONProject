package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.AuthDataSource
import com.woo.peton.core.data.datasource.ImageDataSource
import com.woo.peton.core.data.datasource.ReportDataSource
import com.woo.peton.core.data.mapper.toDomain
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.ReportPostRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class ReportPostRepositoryImpl @Inject constructor(
    private val dataSource: ReportDataSource,
    private val imageDataSource: ImageDataSource,
    private val authDataSource: AuthDataSource
) : ReportPostRepository {
    override fun getPosts(type: ReportType): Flow<List<ReportPost>> =
        dataSource.getPostsFlow(type.name).map { dtoList ->
            coroutineScope {
                val authorIds = dtoList.map { it.authorId }.distinct()

                val userMap = authorIds.map { uid ->
                    async {
                        uid to authDataSource.getUserInfo(uid)
                    }
                }.awaitAll().toMap()

                dtoList.map { dto ->
                    val user = userMap[dto.authorId]
                    val domainPost = dto.toDomain()

                    domainPost.copy(
                        authorName = user?.name ?: dto.authorName,
                        authorProfileImageUrl = user?.profileImageUrl ?: dto.authorProfileImageUrl
                    )
                }
            }
        }

    override fun getPostDetail(id: String): Flow<ReportPost?> =
        dataSource.getPostDetail(id).map { it?.toDomain() }

    override suspend fun addPost(pet: ReportPost): Result<Boolean> = runCatching {
        val finalImageUrl = uploadImageIfNeeded(pet.imageUrl)
        dataSource.addPost(pet.copy(imageUrl = finalImageUrl).toDto())
        true
    }

    override suspend fun updatePost(pet: ReportPost): Result<Boolean> = runCatching {
        val finalImageUrl = uploadImageIfNeeded(pet.imageUrl)
        dataSource.updatePost(pet.copy(imageUrl = finalImageUrl).toDto())
        true
    }

    override suspend fun deletePost(id: String): Result<Boolean> = runCatching {
        dataSource.deletePost(id)
        true
    }

    private suspend fun uploadImageIfNeeded(imageUrl: String): String {
        return imageUrl
            .takeIf { it.isNotBlank() && !it.startsWith("http") }
            ?.let { uri ->
                val fileName = "${UUID.randomUUID()}.jpg"
                imageDataSource.uploadImage(uri, "posts/$fileName").getOrThrow()
            } ?: imageUrl
    }
}