package com.woo.peton.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentFormattedTime(): String {
    // 1. 한국 시간대 정의
    val seoulZone = ZoneId.of("Asia/Seoul")

    // 2. 현재 시간을 한국 시간대로 변환
    // Date 대신 Instant.now()를 사용함
    val nowInSeoul = ZonedDateTime.ofInstant(Instant.now(), seoulZone)

    // 3. 포맷 적용 및 반환
    return nowInSeoul.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}
