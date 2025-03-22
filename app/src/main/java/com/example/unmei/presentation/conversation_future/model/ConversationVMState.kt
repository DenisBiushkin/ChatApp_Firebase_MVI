package com.example.unmei.presentation.conversation_future.model

import android.net.Uri
import androidx.transition.Visibility
import okhttp3.Request
import java.time.LocalDate
import java.time.LocalDateTime

data class ConversationVMState(
    val listMessage: List<MessageListItemUI> = emptyList(),
    val groupedMapMessage : LinkedHashMap<LocalDate,List<MessageListItemUI>> = LinkedHashMap(),
    val selectedUrisForRequest: List<Uri> = emptyList(),
    val selectedMessages: Map<String,Boolean> = emptyMap(),

    val loadingScreen: Boolean= true,
    val contentState: ConversationContentState = ConversationContentState.Loading,
    val chatExistence: Boolean = true,//хз тут ли должен быть

    val loadingOldMessages:Boolean = false,
    val optionsVisibility: Boolean = false,
    val bottomSheetVisibility: Boolean = false,
    val chatFullName:String="",
    val chatIconUrl:String ="",
    val statusChat:String="",

    val groupId : String = "",//хз тут ли должен быть
    val companionId : String = ""//хз тут ли должен быть
)
