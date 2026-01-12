package com.woo.peton.features.mypage.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User

@Composable
fun MyProfileCards(
    user: User,
    pets: List<MyPet>,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. 내 정보 카드
        item {
            UserProfileCard(user = user)
        }

        // 2. 등록된 펫 리스트
        items(pets) { pet ->
            PetProfileCard(pet = pet, onClick = { onPetClick(pet.id) })
        }

        // 3. 추가 버튼 (항상 마지막에 위치)
        item {
            AddPetCard(onClick = onAddPetClick)
        }
    }
}


// 공통 카드 베이스 스타일
@Composable
private fun MyPageCardBase(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        content()
    }
}

@Composable
private fun UserProfileCard(user: User) {
    MyPageCardBase(modifier = Modifier.width(280.dp)) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지 (없으면 기본값)
            AsyncImage(
                model = user.profileImageUrl ?: R.drawable.logo, // TODO: 기본 이미지 리소스 확인 필요
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user.email, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun PetProfileCard(pet: MyPet, onClick: () -> Unit) {
    MyPageCardBase(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("나의 반려동물", fontSize = 10.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = pet.imageUrl, // 모델에 imageUrl이 있다고 가정
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pet.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun AddPetCard(onClick: () -> Unit) {
    MyPageCardBase(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.plus),
                contentDescription = "Add Pet",
                tint = Color.LightGray,
                modifier = Modifier.size(40.dp)
            )
            Text("추가하기", color = Color.LightGray, fontSize = 14.sp)
        }
    }
}