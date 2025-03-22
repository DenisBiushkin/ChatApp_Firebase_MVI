package com.example.unmei.presentation.util.model

import kotlinx.serialization.Serializable

@Serializable
data class NavigateConversationData(
    val chatExist: Boolean = true,
    val chatUrl:String,
    val chatName:String,
    val companionUid: String,
    val chatUid: String? = null,
)