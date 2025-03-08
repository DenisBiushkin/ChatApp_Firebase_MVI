package com.example.unmei.presentation.conversation_future.model

import android.net.Uri
import androidx.transition.Visibility
import okhttp3.Request

data class ConversationVMState(
    val listMessage: List<MessageListItemUI> = emptyList(),
    val selectedUrisForRequest: List<Uri> = emptyList(),
    val selectedMessages: Map<String,Boolean> = emptyMap(),
    val loadingScreen: Boolean= true,
    val loadingOldMessages:Boolean = false,
    val optionsVisibility: Boolean = false,
    val bottomSheetVisibility: Boolean = false,
    val chatFullName:String="",
    val statusChat:String="",
    val groupId : String = "",
    val companionId : String = ""
)
