package com.example.unmei.domain.repository

import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.util.Resource

interface NotificationRepository {

    suspend fun notifySendMessageInRooms(
        roomDetail: RoomDetail,
        message: Message,
        notificationRecipientsId:List<String>
    )
    suspend fun saveNotificationTokenByUserId(
        token:String,
        userid: String
    ): Resource<Unit>

}