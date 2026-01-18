package com.woo.peton.core.data.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.woo.peton.core.data.remote.dto.ReportPostDto
import com.woo.peton.core.data.remote.dto.toDomain
import com.woo.peton.core.data.remote.dto.toDto
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.ReportPostRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReportPostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReportPostRepository {

    // 1. 게시글 목록 가져오기 (실시간 리스너)
    override fun getPosts(type: ReportType): Flow<List<MissingPet>> = callbackFlow {
        val query = firestore.collection("posts")
            .whereEqualTo("reportType", type.name)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val pets = snapshot.documents.mapNotNull { doc ->
                    // 문서 ID를 DTO에 주입 후 도메인 변환
                    doc.toObject(ReportPostDto::class.java)?.copy(id = doc.id)?.toDomain()
                }
                trySend(pets)
            }
        }
        awaitClose { listener.remove() }
    }

    // 2. 상세 정보 가져오기
    override suspend fun getPostDetail(id: String): MissingPet? {
        return try {
            val doc = firestore.collection("posts").document(id).get().await()
            doc.toObject(ReportPostDto::class.java)?.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // (보너스) 게시글 등록하기
    override suspend fun addPost(pet: MissingPet): Result<Boolean> {
        return try {
            // toDto()는 @DocumentId가 있는 id 필드를 제외하고 문서를 만듭니다.
            firestore.collection("posts")
                .add(pet.toDto())
                .await()
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}