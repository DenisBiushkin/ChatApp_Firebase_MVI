package com.example.unmei.presentation.chat_list_feature.model

data class ChatVMState (
    val isOnline :Boolean = false,
    val chatList:List<ChatItemUI> = emptyList(),
    val chatListAdv:List<ChatItemAdvenced> = emptyList(),
    val isLoading:Boolean =true
)