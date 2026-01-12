package com.woo.peton.features.auth.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.woo.peton.core.ui.navigation.AuthNavigationRoute
import com.woo.peton.features.auth.AuthUiState
import com.woo.peton.features.auth.AuthViewModel

@Composable
fun SignUpTabScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableIntStateOf(1) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 회원가입 성공 시 다음 단계(완료 화면)로 이동
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success && currentStep == 4) {
            currentStep = 5 // 완료 화면으로
            viewModel.resetState() // 상태 초기화
        } else if (uiState is AuthUiState.Error) {
            Toast.makeText(context, (uiState as AuthUiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            when (currentStep) {
                1 -> Step1Terms(onNext = { currentStep = 2 })
                2 -> Step2AccountInfo(viewModel = viewModel, onNext = { currentStep = 3 })
                3 -> Step3BasicInfo(viewModel = viewModel, onNext = { currentStep = 4 })
                4 -> Step4PetInfo(viewModel = viewModel, onNext = {
                    // 마지막 단계에서 가입 요청
                    viewModel.requestSignUp()
                })
                5 -> Step5Complete(onGoLogin = {
                    navController.navigate(AuthNavigationRoute.AuthScreen) {
                        popUpTo(AuthNavigationRoute.AuthScreen) { inclusive = true }
                    }
                })
            }
        }
    }
}

// Step 1: 약관 동의 (기존 유지)
@Composable
fun Step1Terms(onNext: () -> Unit) {
    var isChecked1 by remember { mutableStateOf(false) }
    var isChecked2 by remember { mutableStateOf(false) }
    val allChecked = isChecked1 && isChecked2

    Column(modifier = Modifier.fillMaxSize()) {
        Text("서비스 이용을 위해\n약관에 동의해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked1, onCheckedChange = { isChecked1 = it })
            Text("[필수] 서비스 이용 약관 동의")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked2, onCheckedChange = { isChecked2 = it })
            Text("[필수] 개인정보 수집 및 이용 동의")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            enabled = allChecked,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("다음") }
    }
}

// Step 2: 계정 정보 (신규 추가) - 이메일/비밀번호
@Composable
fun Step2AccountInfo(viewModel: AuthViewModel, onNext: () -> Unit) {
    // 뷰모델의 변수가 변경되면 UI도 갱신되도록 처리 (단, 여기선 단순 할당이므로 TextField 자체 상태 이용 권장하지만 간단히 직접 연결)
    // Compose State로 변환하지 않고 직접 변수에 접근하므로, 입력 시 리컴포지션을 위해 rememberUpdatedState 등을 쓰거나
    // TextField 자체에서 state를 관리하고 onNext 시점에 뷰모델에 넣는 것이 더 부드럽습니다.
    // 여기서는 간단하게 로컬 state -> onNext 시 ViewModel 저장을 구현합니다.

    var email by remember { mutableStateOf(viewModel.signUpEmail) }
    var pw by remember { mutableStateOf(viewModel.signUpPassword) }
    var pwCheck by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("로그인에 사용할\n계정 정보를 입력해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pw,
            onValueChange = { pw = it },
            label = { Text("비밀번호 (6자리 이상)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pwCheck,
            onValueChange = { pwCheck = it },
            label = { Text("비밀번호 확인") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            isError = pw.isNotEmpty() && pwCheck.isNotEmpty() && pw != pwCheck
        )

        Spacer(modifier = Modifier.weight(1f))

        val isValid = email.contains("@") && pw.length >= 6 && pw == pwCheck

        Button(
            onClick = {
                viewModel.signUpEmail = email
                viewModel.signUpPassword = pw
                onNext()
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("다음") }
    }
}

// Step 3: 기본 정보 (기존 Step 2)
@Composable
fun Step3BasicInfo(viewModel: AuthViewModel, onNext: () -> Unit) {
    var name by remember { mutableStateOf(viewModel.signUpName) }
    var phone by remember { mutableStateOf(viewModel.signUpPhone) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("사용자 정보를\n입력해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("휴대폰 번호") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                viewModel.signUpName = name
                viewModel.signUpPhone = phone
                onNext()
            },
            enabled = name.isNotEmpty() && phone.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("다음") }
    }
}

// Step 4: 펫 정보 (기존 Step 3) -> 여기서 가입 요청
@Composable
fun Step4PetInfo(viewModel: AuthViewModel, onNext: () -> Unit) {
    var petName by remember { mutableStateOf(viewModel.signUpPetName) }
    var breed by remember { mutableStateOf(viewModel.signUpPetBreed) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("함께하는 반려동물을\n소개해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = petName, onValueChange = { petName = it },
            label = { Text("반려동물 이름") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = breed, onValueChange = { breed = it },
            label = { Text("품종 (예: 말티즈)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                viewModel.signUpPetName = petName
                viewModel.signUpPetBreed = breed
                onNext() // -> viewModel.requestSignUp() 호출됨
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("가입 완료") }
    }
}

// Step 5: 완료 (기존 Step 4)
@Composable
fun Step5Complete(onGoLogin: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("환영합니다!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("회원가입이 성공적으로 완료되었습니다.", color = Color.Gray)
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onGoLogin,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("로그인하러 가기") }
    }
}