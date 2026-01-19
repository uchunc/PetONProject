package com.woo.peton.domain.repository

import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import kotlinx.coroutines.flow.Flow

interface ReportPostRepository {
    fun getPosts(type: ReportType): Flow<List<MissingPet>>
    fun getPostDetail(id: String): Flow<MissingPet?>
    suspend fun addPost(pet: MissingPet): Result<Boolean>
}