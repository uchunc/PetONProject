package com.woo.peton.core.data.impl

import com.woo.peton.core.data.datasource.AuthDataSource
import com.woo.peton.core.data.datasource.ImageDataSource
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
import toDto
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    private val imageDataSource: ImageDataSource
) : AuthRepository {

    override suspend fun signIn(email: String, pw: String): Result<Boolean> = runCatching{
        dataSource.signIn(email, pw)
        true
    }

    override suspend fun signUp(
        email: String,
        pw: String,
        name: String,
        phone: String,
        petInfo: MyPet
    ): Result<Boolean> = runCatching {
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

        true
    }

    override suspend fun updateUserProfile(user: User): Result<Unit> = runCatching {
        val finalImageUrl = user.profileImageUrl
            ?.takeIf { it.isNotBlank() && !it.startsWith("http") }
            ?.let { uriString ->
                val fileName = "${user.uid}_profile_${UUID.randomUUID()}.jpg"
                val path = "users/${user.uid}/profile/$fileName"

                imageDataSource.uploadImage(uriString, path).getOrThrow()
            } ?: user.profileImageUrl

        val updatedUser = user.copy(profileImageUrl = finalImageUrl)
        dataSource.updateUserInfo(updatedUser.toDto())
    }

    override fun getUserProfile(): Flow<User?> {
        val uid = dataSource.getCurrentUserUid() ?: return flowOf(null)

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