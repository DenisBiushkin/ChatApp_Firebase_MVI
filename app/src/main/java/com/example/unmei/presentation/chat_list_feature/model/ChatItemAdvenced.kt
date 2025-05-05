package com.example.unmei.presentation.chat_list_feature.model

import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.messages.RoomSummaries
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.util.toStringTime
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class ChatItemAdvenced(
    val chatRoom: ChatRoomAdvance,
    val status: StatusUser,
    val summaries: RoomSummaries
) {
    fun toUiChatList(currentUsrUid:String):ChatListItemUiAdv {
        val it= this

        return ChatListItemUiAdv(
            chatName = it.chatRoom.chatName,
            chatId = it.chatRoom.id,
            typeChat = it.chatRoom.type,
            iconUrl = it.chatRoom.iconUrl,
            isOnline = it.status.status == Status.ONLINE,
            members = it.chatRoom.members,
            moderators = it.chatRoom.moderators,
            timestamp = it.summaries.lastMessage?.timestamp ?: 0L,
            typingStatus =getTypingStatus(currentUsrUid),
            contentMessage =getContentMessage(currentUsrUid) ,
            unreadedCountMessage = it.summaries.unreadedCount[currentUsrUid] ?: 0,
            lastMessageTimeString = if (it.summaries.lastMessage != null) toStringTime(it.summaries.lastMessage.timestamp) else "",
            messageStatus= MessageStatus.None
        )
    }

    private fun getTypingStatus(currentUsrUid:String):TypingStatus{
        val it= this
        val typingIds = it.summaries.typingUsersStatus - setOf(currentUsrUid)
        return if (typingIds.isNotEmpty()) {
                if (it.chatRoom.type == TypeRoom.PRIVATE) {
                   return TypingStatus.Typing("печатает")
                }else{
                    return TypingStatus.Typing("Участник")
                }
              } else
                 TypingStatus.None
    }
    private fun getContentMessage(currentUsrUid:String):ContentMessage?{
        val it= this
        return if (it.summaries.lastMessage != null) {
            val lastMessage = it.summaries.lastMessage
            var sender = "Вы"

            if (it.chatRoom.type == TypeRoom.PUBLIC) {
                if (it.summaries.lastMessage.senderId != currentUsrUid)
                       sender = "Участник"
            } else {

                if (it.summaries.lastMessage.senderId != currentUsrUid)
                    sender = it.chatRoom.chatName.split(" ").first()
            }
            ContentMessage(
                contentSender = sender.plus(": "),
                message = lastMessage
            )
        } else null
    }

}