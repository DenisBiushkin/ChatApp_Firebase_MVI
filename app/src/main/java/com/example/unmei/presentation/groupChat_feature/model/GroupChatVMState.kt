package com.example.unmei.presentation.groupChat_feature.model

import android.net.Uri

data class GroupChatVMState(
    val chatId:String = "",
    val currentUsrUid:String = "",
    val chatName:String="",
    val chatIconUrl:String="",
    val chatStatus:String="",
    val textMessage:String = "",
    val isTyping:Boolean = false,
    val bottomSheetVisibility:Boolean = false,
    val selectedUrisForRequest: List<Uri> = emptyList(),
    val selectedMessagesIds: Set<String> = emptySet(),
    val optionsVisibility:Boolean = false,
    val screenState: GroupChatScreenState = GroupChatScreenState.LOADING


)


