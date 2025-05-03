package com.example.unmei.presentation.create_group_feature.model

import android.net.Uri

data class CreateGroupVMState(
    val chatName:String="",
    val currentUserId:String = "",
    val chatIconUri: Uri? = null,
    val contentState: CreateGroupContentState= CreateGroupContentState.LOADING,
    val groupedContacts: Map<String,List<CreateGroupItemUi>> = emptyMap(),
    val selectedContacts:  Map<String,CreateGroupItemUi> = emptyMap(),
    val loadingCreatingChat:Boolean = false,
    val navigateInChat:Boolean=false,
    val createdChatId:String="",
)

