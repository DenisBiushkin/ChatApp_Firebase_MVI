package com.example.unmei.data.model

import com.example.unmei.domain.model.RoomSummaries

data class RoomSummariesResp(
        val lastMessage: MessageResponse? = null,
        val unreadedCount : Map<String,Int> = emptyMap(),
        val typingUsersStatus: Map<String,Boolean> = emptyMap()
){
        fun toRoomSummaries(): RoomSummaries = this.run {
                RoomSummaries(
                        lastMessage = lastMessage?.let { it.toMessage() },
                        unreadedCount= unreadedCount,
                        typingUsersStatus = typingUsersStatus.keys.toList()
                )
        }
}