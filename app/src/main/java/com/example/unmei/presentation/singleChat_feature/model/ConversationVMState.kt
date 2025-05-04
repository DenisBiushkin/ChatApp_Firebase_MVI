package com.example.unmei.presentation.singleChat_feature.model

import com.example.unmei.domain.model.AttachmentDraft
import java.time.LocalDate

data class ConversationVMState(
    val textMessage:String="",

    val listMessage: List<MessageListItemUI> = emptyList(),
    val groupedMapMessage : LinkedHashMap<LocalDate,List<MessageListItemUI>> = LinkedHashMap(),

    val selectedMediaForRequest: List<AttachmentDraft> = emptyList(),

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

    val chatId : String = "",//хз тут ли должен быть
    val companionId : String = "",//хз тут ли должен быть,

    val chatState: ChatState = ChatState.Chatting,

    val currentUsrUid:String = "",
    val grouped: Map<LocalDate, List<MessageListItemUI>> = emptyMap(),
    val idIndex: Map<String, MessageListItemUI> = emptyMap(),
    //для пагинации
    val onReached:Boolean=false,
    val lastMessageId:String=""
)

sealed class ChatState{
    object Chatting :ChatState()
    object Create:ChatState()
}
