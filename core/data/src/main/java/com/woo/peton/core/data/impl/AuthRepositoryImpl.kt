package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.AuthDataSource
import com.woo.peton.core.data.mapper.toDto
import com.woo.peton.core.data.remote.dto.UserDto
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User
import com.woo.peton.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import toDomain
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {

    override suspend fun signIn(email: String, pw: String): Result<Boolean> {
        return try {
            dataSource.signIn(email, pw)
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
            val uid = dataSource.createAccount(email, pw)

            val userDto = UserDto(
                uid = uid,
                email = email,
                name = name,
                phoneNumber = phone
            )
            dataSource.saveUserInfo(userDto)

            val petDto = petInfo.copy(ownerId = uid).toDto()
            dataSource.savePetInfo(uid, petDto)

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserProfile(): Flow<User?> {
        val uid = dataSource.getCurrentUserUid()

        uid ?: return flowOf(null)

        return dataSource.getUserInfoFlow(uid)
            .map { dto -> dto?.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override fun signOut() {
        dataSource.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return dataSource.isUserLoggedIn()
    }
}