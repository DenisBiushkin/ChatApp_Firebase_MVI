package com.example.unmei.presentation.conversation_future.model

import com.example.unmei.domain.model.Message

data class ConversationVMState(
    val listMessage: List<ChatListItemUI> = emptyList(),
    val chatFullName:String="",
    val statusChat:String="",
)
