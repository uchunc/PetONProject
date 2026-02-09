package com.woo.peton.core.data.remote.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.woo.peton.core.data.datasource.ReportDataSource
import com.woo.peton.core.data.remote.dto.ReportPostDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReportRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReportDataSource {

    override fun getPostsFlow(reportType: String): Flow<List<ReportPostDto>> = callbackFlow {
        val query = firestore.collection("posts")
            .whereEqualTo("reportType", reportType)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val pets = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ReportPostDto::class.java)?.copy(id = doc.id)
                }
                trySend(pets)
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getPostDetail(id: String): Flow<ReportPostDto?> = callbackFlow {
        val docRef = firestore.collection("posts").document(id)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val dto = snapshot.toObject(ReportPostDto::class.java)?.copy(id = snapshot.id)
                trySend(dto)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun addPost(dto: ReportPostDto) {
        firestore.collection("posts").add(dto).await()
    }

    override suspend fun updatePost(dto: ReportPostDto) {
        if (dto.id.isNotEmpty()) {
            firestore.collection("posts").document(dto.id).set(dto).await()
        }
    }

    override suspend fun deletePost(id: String) {
        firestore.collection("posts").document(id).delete().await()
    }
}