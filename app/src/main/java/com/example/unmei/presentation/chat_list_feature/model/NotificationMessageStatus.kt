package com.example.unmei.presentation.chat_list_feature.model

sealed class NotificationMessageStatus {
    object On:NotificationMessageStatus()
    object Off:NotificationMessageStatus()
}