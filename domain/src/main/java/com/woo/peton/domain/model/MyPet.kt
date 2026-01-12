package com.woo.peton.domain.model

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class MyPet(
    val id: String = "",
    val ownerId: String = "",
    val name: String,
    val gender: String, // "남", "여"
    val breed: String,
    val birthDate: String, // "YYYY-MM-DD" 형식
    val neutered: Boolean = false, // 중성화 여부
    val registrationNumber: String = "", // 동물등록번호
    val content: String = "", // 특징 (성격 등)
    val imageUrl: String = ""
) {
    // 나이 자동 계산 프로퍼티
    val ageText: String
        get() {
            if (birthDate.isBlank()) return ""
            return try {
                val birth = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE)
                val now = LocalDate.now()
                val period = Period.between(birth, now)

                if (period.years >= 1) {
                    "${period.years}살"
                } else {
                    // 1살 미만인 경우 개월 수 (0개월도 포함)
                    "${period.months}개월"
                }
            } catch (e: Exception) {
                ""
            }
        }
}