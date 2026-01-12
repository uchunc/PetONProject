package com.woo.peton.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.R



val Pretendard_R = FontFamily(Font(R.font.pretendard_regular))
val Pretendard_SB = FontFamily(Font(R.font.pretendard_semibold))
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Pretendard_SB,
        fontSize = 22.sp,
        lineHeight = 27.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Pretendard_SB,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Pretendard_SB,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Pretendard_R,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp
    )
)


