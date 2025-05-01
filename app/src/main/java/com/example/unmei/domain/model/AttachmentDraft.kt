package com.example.unmei.domain.model

import android.net.Uri

data class AttachmentDraft(
    val uri: Uri,
    val mimeType: String,
    val extraInfo: Map<String, Any> = emptyMap() // для duration, filename и т.п.
)