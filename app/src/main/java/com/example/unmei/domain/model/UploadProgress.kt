package com.example.unmei.domain.model

import android.net.Uri
import com.example.unmei.domain.model.messages.Attachment

sealed class UploadProgress {

    data class Uploading(
        val uri: Uri,
        val progress: Float
    ) : UploadProgress() // от 0.0 до 1.0

    data class Success(
        val uri: Uri,
        val attachment: Attachment
    ) : UploadProgress()

    data class Failed(
        val exception: Exception
    ) : UploadProgress()
}