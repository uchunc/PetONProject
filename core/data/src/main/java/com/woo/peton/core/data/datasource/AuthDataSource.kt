// core/data/src/main/java/com/woo/peton/core/data/datasource/AuthDataSource.kt

package com.woo.peton.core.data.datasource

import com.woo.peton.core.data.remote.dto.MyPetDto
import com.woo.peton.core.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    suspend fun signIn(email: String, pw: String)
    suspend fun createAccount(email: String, pw: String): String
    suspend fun saveUserInfo(userDto: UserDto)
    suspend fun savePetInfo(uid: String, petDto: MyPetDto)

    fun isUserLoggedIn(): Boolean
    fun signOut()
    fun getCurrentUserUid(): String?

    fun getUserInfoFlow(uid: String): Flow<UserDto?>
}