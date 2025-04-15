package com.example.unmei.data.model

data class FcmMessage(
    val message: NtfMessage
)

data class NtfMessage(
    val token: String, // токен устройства
    val notification: Notification,
)

data class Notification(
    val title: String,
    val body: String
)