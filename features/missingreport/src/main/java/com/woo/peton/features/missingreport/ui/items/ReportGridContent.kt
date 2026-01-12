package com.woo.peton.features.missingreport.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woo.peton.core.ui.component.PetCardVertical
import com.woo.peton.domain.model.MissingPet

@Composable
fun ReportGridContent(
    pets: List<MissingPet>,
    onItemClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(pets) { missingPet ->
            // 앞서 정의한 Core UI의 PetCardVertical 사용
            PetCardVertical(
                pet = missingPet, // 파라미터 이름을 pet으로 통일 (이전 구현 참고)
                onClick = { onItemClick(missingPet.id) }
            )
        }
    }
}