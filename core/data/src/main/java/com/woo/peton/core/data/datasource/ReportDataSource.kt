package com.woo.peton.core.data.datasource

import com.woo.peton.core.data.remote.dto.ReportPostDto
import kotlinx.coroutines.flow.Flow

interface ReportDataSource {
    fun getPostsFlow(reportType: String): Flow<List<ReportPostDto>>

    fun getPostDetail(id: String): Flow<ReportPostDto?>

    suspend fun addPost(dto: ReportPostDto)
    suspend fun updatePost(dto: ReportPostDto)
    suspend fun deletePost(id: String)
}