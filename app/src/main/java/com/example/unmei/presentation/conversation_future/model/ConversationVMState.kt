package com.example.unmei.presentation.conversation_future.model

import androidx.transition.Visibility

data class ConversationVMState(
    val listMessage: List<MessageListItemUI> = emptyList(),
    val selectedMessages: Map<String,Boolean> = emptyMap(),
    val loadingScreen: Boolean= true,
    val loadingOldMessages:Boolean = false,
    val optionsVisibility: Boolean = false,
    val chatFullName:String="",
    val statusChat:String="",
    val groupId : String = "",
    val companionId : String = ""
)
