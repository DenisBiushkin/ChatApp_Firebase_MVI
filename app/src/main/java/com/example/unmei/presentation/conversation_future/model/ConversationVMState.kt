package com.example.unmei.presentation.conversation_future.model

import android.net.Uri
import java.time.LocalDate

data class ConversationVMState(
    val listMessage: List<MessageListItemUI> = emptyList(),
    val groupedMapMessage : LinkedHashMap<LocalDate,List<MessageListItemUI>> = LinkedHashMap(),
    val selectedUrisForRequest: List<Uri> = emptyList(),
    val selectedMessages: Map<String,Boolean> = emptyMap(),

    val loadingScreen: Boolean= true,
    val contentState: ContentStateScreen = ContentStateScreen.Loading,
    val chatExistence: Boolean = true,//хз тут ли должен быть

    val loadingOldMessages:Boolean = false,
    val optionsVisibility: Boolean = false,
    val bottomSheetVisibility: Boolean = false,

    val chatFullName:String="",
    val chatIconUrl:String ="",
    val statusChat:String ="",
    val isTyping:Boolean = false,

    val groupId : String = "",//хз тут ли должен быть
    val companionId : String = ""//хз тут ли должен быть
)
