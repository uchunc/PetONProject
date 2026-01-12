package com.woo.peton.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.R

@Composable
fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier // 외부에서 여백 조절 가능하게
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.clickable { onMoreClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "더보기", fontSize = 12.sp, color = Color.Gray)
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrowr),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}