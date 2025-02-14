package com.example.unmei.domain.mapper

import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.domain.model.ChatRoom
import org.example.Mappers.base.Mapper
import javax.inject.Inject

class ChatRoomMapper @Inject constructor(): Mapper<ChatRoomResponse, ChatRoom> {
    override fun map(from: ChatRoomResponse): ChatRoom = from.run {
        ChatRoom(
           id = id,
            type=type,
            timestamp = if(timestamp is Long) timestamp else 0L,
            moderators = moderators,
            members = members,
            lastMessage = lastMessage
        )
    }
}