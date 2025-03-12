package com.example.unmei.presentation.conversation_future.model

import android.net.Uri

sealed class ConversationEvent {

    object LoadingNewMessage :ConversationEvent()
    data class ChangeSelectedMessages (val id:String) :ConversationEvent()

    object OpenCloseBottomSheet :ConversationEvent()
    data class  SelectedMediaToSend(val value: List<Uri>):ConversationEvent()
    data class  SendMessage(val text:String):ConversationEvent()
}