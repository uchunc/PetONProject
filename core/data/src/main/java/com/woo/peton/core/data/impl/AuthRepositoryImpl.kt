package com.woo.peton.core.data.impl

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.woo.peton.core.data.dto.toDto
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User
import com.woo.peton.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signIn(email: String, pw: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pw).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        email: String,
        pw: String,
        name: String,
        phone: String,
        petInfo: MyPet
    ): Result<Boolean> {
        return try {
            // 1. Firebase Auth에 계정 생성
            val authResult = auth.createUserWithEmailAndPassword(email, pw).await()
            val uid = authResult.user?.uid ?: throw Exception("UID 생성 실패")

            // 2. Firestore - Users 컬렉션에 사용자 기본 정보 저장
            val userMap = hashMapOf(
                "uid" to uid,
                "email" to email,
                "name" to name,
                "phone" to phone,
                "createdAt" to Timestamp.now()
            )
            firestore.collection("users").document(uid).set(userMap).await()

            // 3. Firestore - MyPets 서브 컬렉션에 펫 정보 저장
            // (이미지 업로드는 일단 제외하고 URL이 비어있다고 가정)
            val petDto = petInfo.copy(ownerId = uid).toDto() // toDto()는 이전에 만든 매퍼 사용

            firestore.collection("users").document(uid)
                .collection("my_pets")
                .add(petDto)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            // 실패 시 생성된 계정이 있다면 삭제하는 롤백 로직이 있으면 좋지만, 일단 에러 반환
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): Result<User> {  // flow로 바꾸기?
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("로그인이 필요합니다.")
            val snapshot = firestore.collection("users").document(uid).get().await()

            if (snapshot.exists()) {
                val user = User(
                    uid = uid,
                    name = snapshot.getString("name") ?: "",
                    email = snapshot.getString("email") ?: "",
                    phoneNumber = snapshot.getString("phone") ?: ""
                )
                Result.success(user)
            } else {
                Result.failure(Exception("사용자 정보를 찾을 수 없습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}