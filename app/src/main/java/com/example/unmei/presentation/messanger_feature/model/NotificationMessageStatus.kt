package com.example.unmei.presentation.messanger_feature.model

sealed class NotificationMessageStatus {
    object On:NotificationMessageStatus()
    object Off:NotificationMessageStatus()
}