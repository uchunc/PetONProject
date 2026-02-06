package com.woo.peton.features.missingreport.ui.items.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.features.missingreport.ui.screen.ReportPostContent

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
        when (selectedPet) {
            null -> {
                ReportGridContent(
                    pets = pets,
                    onItemClick = onItemClick
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    ReportPostContent(
                        pet = selectedPet,
                        contentMaxLines = 3,
                        showMap = false
                    )

                    Button(
                        onClick = { onItemClick(selectedPet.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 30.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00))
                    ) {
                        Text(
                            text = "게시글 확인하기",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}