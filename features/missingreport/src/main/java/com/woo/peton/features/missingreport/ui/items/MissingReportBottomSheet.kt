package com.woo.peton.features.missingreport.ui.items

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.woo.peton.domain.model.ReportPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportBottomSheet(
    modifier: Modifier = Modifier,
    pets: List<ReportPost>,
    selectedPet: ReportPost?,
    onItemClick: (String) -> Unit,
    onBackToList: () -> Unit
) {
    BackHandler(enabled = selectedPet != null) {
        onBackToList()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReportGridContent(
            pets = pets,
            onItemClick = onItemClick
        )
    }
}