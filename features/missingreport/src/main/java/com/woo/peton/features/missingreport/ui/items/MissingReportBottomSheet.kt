package com.woo.peton.features.missingreport.ui.items

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.woo.peton.domain.model.MissingPet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportBottomSheet(
    modifier: Modifier = Modifier,
    pets: List<MissingPet>,
    selectedPet: MissingPet?,
    onItemClick: (String) -> Unit,
    onBackToList: () -> Unit
) {
    BackHandler(enabled = selectedPet != null) {
        onBackToList()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .width(32.dp)
            )
            ReportGridContent(
                pets = pets,
                onItemClick = onItemClick
            )
        }
    }
}