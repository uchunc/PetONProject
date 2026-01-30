package com.woo.peton.core.data.remote.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.woo.peton.core.data.datasource.AuthDataSource
import com.woo.peton.core.data.remote.dto.MyPetDto
import com.woo.peton.core.data.remote.dto.UserDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthDataSource {
    override suspend fun signIn(email: String, pw: String) {
        auth.signInWithEmailAndPassword(email, pw).await()
    }

    override suspend fun createAccount(email: String, pw: String): String {
        val result = auth.createUserWithEmailAndPassword(email, pw).await()
        return result.user?.uid ?: throw Exception("UID 생성 실패")
    }

    override suspend fun saveUserInfo(userDto: UserDto) {
        firestore.collection("users").document(userDto.uid)
            .set(userDto, SetOptions.merge())
            .await()
    }

    override suspend fun updateUserInfo(userDto: UserDto) {
        firestore.collection("users").document(userDto.uid)
            .set(userDto, SetOptions.merge())
            .await()
    }

    override suspend fun savePetInfo(uid: String, petDto: MyPetDto) {
        firestore.collection("users").document(uid)
            .collection("my_pets")
            .add(petDto)
            .await()
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserUid(): String? = auth.currentUser?.uid

    override fun getUserInfoFlow(uid: String): Flow<UserDto?> = callbackFlow {
        val docRef = firestore.collection("users").document(uid)

        val listenerRegistration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val userDto = snapshot.toObject(UserDto::class.java)
                trySend(userDto)
            } else {
                trySend(null)
            }
        }
        awaitClose { listenerRegistration.remove() }
    }
}