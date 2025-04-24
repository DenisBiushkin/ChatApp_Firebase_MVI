package com.example.unmei.presentation.chat_list_feature.model

import com.example.unmei.data.model.ChatRoomAdvence
import com.example.unmei.domain.model.messages.RoomSummaries
import com.example.unmei.domain.model.StatusUser

data class ChatItemAdvenced(
    val chatRoom: ChatRoomAdvence,
    val status: StatusUser,
    val summaries: RoomSummaries
) {
}