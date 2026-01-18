package com.woo.peton.domain.repository

import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, pw: String): Result<Boolean>

    suspend fun signUp(
        email: String,
        pw: String,
        name: String,
        phone: String,
        petInfo: MyPet
    ): Result<Boolean>

    fun signOut()

    fun isUserLoggedIn(): Boolean

    fun getUserProfile(): Flow<User?>
}