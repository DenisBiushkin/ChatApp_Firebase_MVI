package com.example.unmei.presentation.singleChat_feature.model

import androidx.compose.ui.graphics.vector.ImageVector

data class AttachmentOption(
    val label: String,
    val icon: ImageVector,
    val selected: Boolean = false
)