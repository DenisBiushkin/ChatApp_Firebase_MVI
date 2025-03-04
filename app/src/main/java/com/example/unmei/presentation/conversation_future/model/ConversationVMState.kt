package com.example.unmei.presentation.conversation_future.model

import com.example.unmei.presentation.chat_list_feature.model.ChatListItemUI

data class ConversationVMState(
    val listMessage: List<MessageListItemUI> = emptyList(),
    val loading: Boolean= true,
    val chatFullName:String="",
    val statusChat:String="",
    val groupId : String = "",
    val companionId : String = ""
)
