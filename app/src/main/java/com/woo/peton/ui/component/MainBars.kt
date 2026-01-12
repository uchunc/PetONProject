package com.woo.peton.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.R

// 🟢 메인 탑바 UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    topBarData: TopBarData,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.shadow(elevation = 4.dp),
        title = {
            topBarData.title.takeIf { it.isNotEmpty() }?.let { titleText ->
                Text(text = titleText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        navigationIcon = {
            topBarData.titleIcon?.let { icon ->
                val arrowIcon = ImageVector.vectorResource(R.drawable.arrowl)
                val closeIcon = ImageVector.vectorResource(R.drawable.close)

                if (icon == arrowIcon || icon == closeIcon) {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = icon, contentDescription = "Back")
                    }
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        },
        actions = {
            topBarData.actionIcon?.let { icon ->
                IconButton(onClick = onActionClick) {
                    Icon(imageVector = icon, contentDescription = "Action", tint = Color.Unspecified)
                }
            }
        }
    )
}

// 🟢 메인 바텀바 UI
@Composable
fun MainBottomBar(
    items: List<BottomAppBarItem>,
    currentRoute: String,
    onNavigate: (Any) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.shadow(elevation = 8.dp)
    ) {
        items.forEach { bottomItem ->
            // Type-Safe Route는 클래스명을 포함하므로 contains로 확인
            val isSelected = currentRoute.contains(bottomItem.destination::class.qualifiedName.toString())

            NavigationBarItem(
                selected = isSelected,
                label = {
                    Text(
                        text = bottomItem.tabName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = bottomItem.icon),
                        contentDescription = bottomItem.tabName
                    )
                },
                onClick = { onNavigate(bottomItem.destination) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}