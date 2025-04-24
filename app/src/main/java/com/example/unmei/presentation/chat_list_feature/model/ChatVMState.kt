package com.example.unmei.presentation.chat_list_feature.model

import com.example.unmei.presentation.conversation_future.model.ContentStateScreen

data class ChatVMState (
    val chatList:List<ChatItemUI> = emptyList(),
    val chatListAdv:List<ChatListItemUiAdv> = emptyList(),

    val contentState: ContentStateScreen = ContentStateScreen.Loading,

    val isOnline :Boolean = false,
    val fullName:String ="",
    val iconUrl:String="",
    val signInData:String="",
    val userId:String=""

)