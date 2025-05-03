package com.example.unmei.presentation.groupChat_feature.model

import android.net.Uri
import com.example.unmei.domain.model.AttachmentDraft
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.presentation.singleChat_feature.model.ContentStateScreen
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.google.firebase.Timestamp
import java.time.LocalDate

data class GroupChatVMState(
    val chatId:String = "",
    val members:Set<String> = emptySet(),
    val moderators:Set<String> = emptySet(),
    val actualUserExtendedMap: Map<String,UserExtended> = emptyMap(),
    val timestamp:Long = 0L,
    val currentUsrUid:String = "",
    val chatName:String="",
    val chatIconUrl:String="",
    val chatStatus:String="",
    val textMessage:String = "",
    val isTyping:Boolean = false,
    val bottomSheetVisibility:Boolean = false,
    val optionsVisibility:Boolean = false,
    val contentState: ContentStateScreen = ContentStateScreen.Loading,
    val selectedMediaForRequest: List<AttachmentDraft> = emptyList(),
    val selectedMessagesIds: Set<String> = emptySet(),
    val listMessage: List<MessageListItemUI> = emptyList(),
    val selectedMessages: Map<String,Boolean> = emptyMap(),


    val grouped: Map<LocalDate, List<MessageListItemUI>> = emptyMap(),
    val idIndex: Map<String, MessageListItemUI> = emptyMap(),
    //для пагинации
    val loadingOldMessages:Boolean = false,
    val onReached:Boolean=false,
    val lastMessageId:String=""
)


