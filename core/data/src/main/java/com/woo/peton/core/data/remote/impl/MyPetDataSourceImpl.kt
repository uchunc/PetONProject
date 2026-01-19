package com.woo.peton.core.data.remote.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.woo.peton.core.data.datasource.MyPetDataSource
import com.woo.peton.core.data.remote.dto.MyPetDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MyPetDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MyPetDataSource {
    override fun getAllMyPets(): Flow<List<MyPetDto>> {
        val uid = auth.currentUser?.uid ?: return flowOf(emptyList())

        return callbackFlow {
            val collectionRef = firestore.collection("users").document(uid).collection("my_pets")

            val listener = collectionRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val pets = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(MyPetDto::class.java)
                    }
                    trySend(pets)
                }
            }
            awaitClose { listener.remove() }
        }
    }

    override suspend fun getPetById(petId: String): MyPetDto? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = firestore.collection("users").document(uid)
            .collection("my_pets").document(petId)
            .get().await()
        return snapshot.toObject(MyPetDto::class.java)
    }

    override suspend fun savePet(petDto: MyPetDto) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val collectionRef = firestore.collection("users").document(uid).collection("my_pets")

        val docRef = if (petDto.id.isBlank()) {
            collectionRef.document()
        } else {
            collectionRef.document(petDto.id)
        }

        val finalDto = petDto.copy(id = docRef.id, ownerId = uid)
        docRef.set(finalDto).await()
    }
}