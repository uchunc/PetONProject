package com.woo.peton.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 1. 다크 모드 색상 매핑
private val DarkColorScheme = darkColorScheme(
    primary = Primary_N,
    onPrimary = Color.White,
    secondary = Secondary_N,
    tertiary = Secondary_L,

    // 배경색을 어두운 회색으로 매핑
    background = GrayScale2C,
    surface = GrayScale2C,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = GrayScale7E
)

// 2. 라이트 모드 색상 매핑
private val LightColorScheme = lightColorScheme(
    primary = Primary_N,
    onPrimary = Color.White,
    secondary = Secondary_N,
    tertiary = Secondary_L,

    // 배경색을 밝은 회색/흰색으로 매핑
    background = Color.White, // 또는 GrayScaleE9
    surface = Color.White,
    onBackground = GrayScale2C, // 글자색
    onSurface = GrayScale2C,
    outline = GrayScaleAA
)

@Composable
fun PetONProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Android 12+ 동적 컬러 사용 여부 (브랜드 컬러 유지를 위해 보통 false 추천)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}