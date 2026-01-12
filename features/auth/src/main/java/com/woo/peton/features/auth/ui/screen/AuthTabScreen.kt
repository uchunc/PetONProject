package com.woo.peton.features.auth.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.component.CirclePlaceholder
import com.woo.peton.core.ui.navigation.AuthNavigationRoute
import com.woo.peton.core.ui.navigation.HomeNavigationRoute
import com.woo.peton.features.auth.AuthUiState
import com.woo.peton.features.auth.AuthViewModel

@Composable
fun AuthTabScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var startAnimation by remember { mutableStateOf(false) }
    var idText by remember { mutableStateOf("") } // 실제로는 이메일 입력
    var pwText by remember { mutableStateOf("") }

    // ViewModel 상태 구독
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 로그인 결과 처리 (성공 시 홈으로, 실패 시 토스트)
    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                navController.navigate(HomeNavigationRoute.HomeScreen) {
                    popUpTo(0) { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthUiState.Error -> {
                val msg = (uiState as AuthUiState.Error).message
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // 애니메이션 설정
    val logoSize = 150.dp
    val finalTopPadding = 60.dp
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val screenHeight = maxHeight
        val centerPadding = (screenHeight / 2) - (logoSize / 2)

        val animatedTopPadding by animateDpAsState(
            targetValue = if (startAnimation) finalTopPadding else centerPadding,
            animationSpec = tween(durationMillis = 1000),
            label = "LogoAnimation"
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(animatedTopPadding))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(logoSize)
            )
            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInVertically(
                    initialOffsetY = { with(density) { 100.dp.roundToPx() } },
                    animationSpec = tween(durationMillis = 1000)
                ) + fadeIn(animationSpec = tween(durationMillis = 1000))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // --- ID (이메일) 입력 ---
                    OutlinedTextField(
                        value = idText,
                        onValueChange = { idText = it },
                        label = { Text("이메일") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- 비밀번호 입력 ---
                    OutlinedTextField(
                        value = pwText,
                        onValueChange = { pwText = it },
                        label = { Text("비밀번호") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // --- 로그인 버튼 (로직 연결됨) ---
                    Button(
                        onClick = {
                            if (idText.isNotEmpty() && pwText.isNotEmpty()) {
                                viewModel.login(idText, pwText)
                            } else {
                                Toast.makeText(context, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = uiState !is AuthUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("로그인", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // ==========================================
                    // 👇 기존 코드 유지 영역 (소셜 로그인 & 회원가입)
                    // ==========================================
                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = "소셜 로그인",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CirclePlaceholder(
                            color = Color(0xFF03C75A),
                            text = "N",
                            onClick = { /* 네이버 로그인 로직 추후 구현 */ }
                        )
                        CirclePlaceholder(
                            color = Color(0xFFFEE500),
                            text = "K",
                            onClick = { /* 카카오 로그인 로직 추후 구현 */ }
                        )
                        CirclePlaceholder(
                            color = Color(0xFFEA4335),
                            text = "G",
                            onClick = { /* 구글 로그인 로직 추후 구현 */ }
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))

                    val signUpText = buildAnnotatedString {
                        append("아직 회원이 아니신가요?  ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("회원가입")
                        }
                    }
                    Text(
                        text = signUpText,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(AuthNavigationRoute.SignUpScreen)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // ==========================================
                }
            }
        }
    }
}