package com.example.unmei.presentation.chat_list_feature.model

sealed class MessageStatus {
    object Loading:MessageStatus()
    object Send:MessageStatus()
    object Readed:MessageStatus()
    object Error:MessageStatus()
    object None:MessageStatus()
}