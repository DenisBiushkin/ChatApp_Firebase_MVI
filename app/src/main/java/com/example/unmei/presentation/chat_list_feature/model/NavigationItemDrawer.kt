package com.example.unmei.presentation.chat_list_feature.model


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItemDrawer(
    val title:String,
    val imageVector: ImageVector,
    val navRoute:String="",
    val colorIcon: Color = Color.Black.copy(alpha = 0.5f)
)