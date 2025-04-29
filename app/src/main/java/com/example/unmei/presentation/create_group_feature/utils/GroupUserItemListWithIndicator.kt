package com.example.unmei.presentation.create_group_feature.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun GroupUserItemListWithIndicator(
    painter: Painter,
    fullName: String,
    lastSeen:String,
    isSelected:Boolean = true,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        CircleSelectItemBox(isSelected = isSelected)
        GroupUserItemList(
            painter,
            fullName,
            lastSeen,
            onClick = {},
            clickEnable = false
        )
    }
}