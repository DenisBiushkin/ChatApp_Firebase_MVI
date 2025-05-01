package com.example.unmei.presentation.singleChat_feature.model

import com.example.unmei.domain.model.AttachmentDraft

sealed class ConversationEvent {

    object LoadingNewMessage :ConversationEvent()
    data class ChangeSelectedMessages (val id:String) :ConversationEvent()

    object OpenCloseBottomSheet :ConversationEvent()
    data class  SelectedMediaToSend(val value: List<AttachmentDraft>):ConversationEvent()

    object OffOptions:ConversationEvent()
    object LeftChat:ConversationEvent()

    object DeleteSelectedMessages:ConversationEvent()


    data class OnValueChangeTextMessage(
        val text:String
    ):ConversationEvent()

    object  SendMessage:ConversationEvent()

}