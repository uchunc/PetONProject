package com.woo.peton.domain.repository

import com.woo.peton.domain.model.MyPet


interface MyPetRepository {
    // 1. 목록 조회 (홈 화면, 마이페이지 공용)
    suspend fun getAllMyPets(): List<MyPet>

    // 2. 단건 상세 조회 (수정 화면 진입 시 사용)
    suspend fun getPetById(petId: String): MyPet?

    // 3. 생성 및 수정 (ID 유무에 따라 자동 분기)
    suspend fun savePet(myPet: MyPet): Result<Unit>

    // (선택 사항) 추후 삭제 기능이 필요하다면 여기에 추가하면 됩니다.
    // suspend fun deletePet(petId: String): Result<Unit>
}