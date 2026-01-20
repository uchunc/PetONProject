package com.woo.peton.domain.repository

import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType
import kotlinx.coroutines.flow.Flow

interface ReportPostRepository {
    fun getPosts(type: ReportType): Flow<List<ReportPost>>
    fun getPostDetail(id: String): Flow<ReportPost?>
    suspend fun addPost(pet: ReportPost): Result<Boolean>
}