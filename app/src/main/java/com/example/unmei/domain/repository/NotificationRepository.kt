package com.example.unmei.domain.repository

import com.example.unmei.domain.model.RoomDetail
import com.example.unmei.util.Resource

interface NotificationRepository {

    suspend fun notifySendMessageInRooms(
        roomDetail: RoomDetail,
        notificationRecipientsId:List<String>
    )
    suspend fun saveNotificationTokenByUserId(
        token:String,
        userid: String
    ): Resource<Unit>

}