package com.example.unmei.presentation.singleChat_feature.model

import android.net.Uri

data class AttachmentUi(
    val uri: Uri?=null,
    val type: AttachmentTypeUI,
    val uploadedUrl: String? = null,
    val isLoading: Boolean = false,
    val progressValue: Float = 0f
)

enum class AttachmentTypeUI{
    IMAGE,
    AUDIO,
    VIDEO,
    FILE
}