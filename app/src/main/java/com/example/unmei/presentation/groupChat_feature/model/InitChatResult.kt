package com.example.unmei.presentation.groupChat_feature.model

sealed class InitChatResult {
    object Success : InitChatResult()
    data class Failure(val reason: String) : InitChatResult()
}