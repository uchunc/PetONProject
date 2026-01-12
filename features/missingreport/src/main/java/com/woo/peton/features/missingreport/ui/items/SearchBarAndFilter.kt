package com.woo.peton.features.missingreport.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.ReportType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarAndFilter(
    modifier: Modifier = Modifier,
    filters: Map<ReportType, Boolean>,
    onFilterToggle: (ReportType) -> Unit,
    onHeightMeasured: (Int) -> Unit // 높이 전달용 콜백
) {
    // 높이 측정을 위해 겉을 Box로 감쌉니다.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                onHeightMeasured(coordinates.size.height)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            // 검색창
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp)
            ) {
                Icon(ImageVector.vectorResource(R.drawable.search), contentDescription = "검색", tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("지역 또는 동물 검색", color = Color.Gray, modifier = Modifier.weight(1f))
                Icon(ImageVector.vectorResource(R.drawable.filter), contentDescription = "필터", tint = Color.Black)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 필터 칩
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ReportType.entries.forEach { type ->
                    val isSelected = filters[type] == true
                    val typeColor = Color(type.colorHex)

                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterToggle(type) },
                        label = {
                            Text(
                                text = type.label,
                                color = if (isSelected) Color.White else typeColor,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = typeColor,
                            containerColor = Color.White,
                            labelColor = typeColor,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = typeColor,
                            selectedBorderColor = typeColor,
                            borderWidth = 1.dp
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = FilterChipDefaults.filterChipElevation(elevation = 2.dp)
                    )
                }
            }
        }
    }
}