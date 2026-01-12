package com.woo.peton.domain.repository

import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User

interface AuthRepository {
    // 1. 로그인
    suspend fun signIn(email: String, pw: String): Result<Boolean>

    // 2. 회원가입 (Auth 계정 생성 + Firestore 유저 정보 + 펫 정보)
    suspend fun signUp(
        email: String,
        pw: String,
        name: String,
        phone: String,
        petInfo: MyPet // 펫 정보도 같이 저장
    ): Result<Boolean>

    // 3. 로그아웃
    fun signOut()

    // 4. 현재 로그인 상태 확인
    fun isUserLoggedIn(): Boolean

    suspend fun getUserProfile(): Result<User>
}