package com.woo.peton.core.utils

import com.google.firebase.Timestamp // 🟢 [추가] Firebase Timestamp
import java.time.LocalDateTime
import java.time.ZoneId // 🟢 [추가] 시간대 변환용
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date // 🟢 [추가] Date 변환용
import java.util.Locale

fun LocalDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREA)
    return this.format(formatter)
}

// (선택) "방금 전", "3일 전" 처럼 보여주는 상대 시간 함수
fun LocalDateTime.toRelativeString(): String {
    val now = LocalDateTime.now()
    val minutes = ChronoUnit.MINUTES.between(this, now)
    val hours = ChronoUnit.HOURS.between(this, now)
    val days = ChronoUnit.DAYS.between(this, now)

    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 전"
        days < 7 -> "${days}일 전"
        else -> this.toFormattedString()
    }
}

// ==========================================================
// 2. 추가된 코드 (Firestore 연동용)
// ==========================================================

/**
 * Firestore Timestamp -> Java LocalDateTime 변환
 * 값이 null이면 현재 시간을 반환하여 NullPointerException 방지
 */
fun Timestamp?.toLocalDateTime(): LocalDateTime {
    return this?.toDate()?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?: LocalDateTime.now()
}

/**
 * Java LocalDateTime -> Firestore Timestamp 변환
 * 값이 null이면 현재 서버 시간을 기준으로 Timestamp 생성
 */
fun LocalDateTime?.toTimestamp(): Timestamp {
    return if (this != null) {
        val date = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
        Timestamp(date)
    } else {
        Timestamp.now()
    }
}