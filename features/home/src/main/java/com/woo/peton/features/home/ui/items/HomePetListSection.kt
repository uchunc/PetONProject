package com.woo.peton.features.home.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.component.PetCard
import com.woo.peton.core.ui.component.SectionHeader
import com.woo.peton.domain.model.MissingPet

@Composable
fun HomePetListSection(
    title: String,
    pets: List<MissingPet>,
    onPetClick: (String) -> Unit,
    onViewAllClick: () -> Unit
) {
    if (pets.isNotEmpty()) {
        Column {
            SectionHeader(
                title = title,
                onMoreClick = onViewAllClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val displayPets = pets.take(10)

                items(displayPets) { pet ->
                    PetCard(
                        pet = pet,
                        onClick = { onPetClick(pet.id) },
                        modifier = Modifier.width(160.dp)
                    )
                }
                item {
                    ViewAllCard(
                        onClick = onViewAllClick
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ViewAllCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 원형 화살표 아이콘
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrowr),
                        contentDescription = "전체보기",
                        tint = colorScheme.tertiary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "전체보기",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.tertiary
                )
            }
        }
    }
}