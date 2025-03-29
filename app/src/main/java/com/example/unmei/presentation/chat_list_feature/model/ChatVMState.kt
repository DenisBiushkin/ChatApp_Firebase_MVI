package com.example.unmei.presentation.chat_list_feature.model

data class ChatVMState (
    val chatList:List<ChatItemUI> = emptyList(),
    val chatListAdv:List<ChatListItemUiAdv> = emptyList(),
    val isLoading:Boolean =true,
    val isOnline :Boolean = false,
    val fullName:String ="",
    val iconUrl:String="",
    val signInData:String="",
    val userId:String=""

)