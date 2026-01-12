package com.woo.peton.domain.repository

import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import kotlinx.coroutines.flow.Flow

interface ReportPostRepository {
    // type을 넘겨주면 그 타입의 글만 가져옴 (MISSING or PROTECTION)
    fun getPosts(type: ReportType): Flow<List<MissingPet>>
    suspend fun getPostDetail(id: String): MissingPet?
    suspend fun addPost(pet: MissingPet): Result<Boolean>
}