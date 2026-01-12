package com.woo.peton.features.missingreport.ui.items

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woo.peton.core.ui.component.PetCardHorizontal
import com.woo.peton.domain.model.MissingPet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportBottomSheet(
    modifier: Modifier = Modifier,
    height: Dp,
    pets: List<MissingPet>,
    selectedPet: MissingPet?, // 🟢 선택된 펫 (nullable)
    onItemClick: (String) -> Unit,
    onDetailClick: () -> Unit,
    onBackToList: () -> Unit
) {
    // 상세 정보 모드일 때, 뒤로가기 버튼(하드웨어)을 누르면 리스트로 복귀
    BackHandler(enabled = selectedPet != null) {
        onBackToList()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. 공통 손잡이
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .width(32.dp)
            )

            // 2. 내용물 (상태에 따라 전환)
            Box(modifier = Modifier.weight(1f)) {
                if (selectedPet != null) {
                    // [B] 상세 요약 모드 (가로형 카드)
                    // Box로 감싸서 중앙 정렬 혹은 상단 배치
                    Box(modifier = Modifier.fillMaxSize()) {
                        PetCardHorizontal(
                            pet = selectedPet,
                            onDetailClick = onDetailClick
                        )
                    }
                } else {
                    // [A] 리스트 모드 (그리드)
                    ReportGridContent(
                        pets = pets,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}