package com.woo.peton.core.data.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.woo.peton.core.data.mapper.toDomain
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.core.data.remote.dto.MyPetDto
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.repository.MyPetRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MyPetRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MyPetRepository {

    // 2. 내 모든 반려동물 가져오기 (마이페이지용)
    override suspend fun getAllMyPets(): List<MyPet> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = firestore.collection("users").document(uid)
                .collection("my_pets")
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(MyPetDto::class.java)?.toDomain()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPetById(petId: String): MyPet? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val snapshot = firestore.collection("users").document(uid)
                .collection("my_pets").document(petId)
                .get().await()
            snapshot.toObject(MyPetDto::class.java)?.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 🟢 저장 (ID가 없으면 생성, 있으면 수정)
    override suspend fun savePet(myPet: MyPet): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
        return try {
            val collectionRef = firestore.collection("users").document(uid).collection("my_pets")

            val docRef = if (myPet.id.isBlank()) {
                collectionRef.document() // 새 문서 생성
            } else {
                collectionRef.document(myPet.id) // 기존 문서 참조
            }

            // ID가 비어있었다면 생성된 ID 할당
            val petToSave = myPet.copy(id = docRef.id, ownerId = uid)

            docRef.set(petToSave.toDto()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}