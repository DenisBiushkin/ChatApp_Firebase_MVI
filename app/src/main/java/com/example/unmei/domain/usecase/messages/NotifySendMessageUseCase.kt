package com.example.unmei.domain.usecase.messages

import com.example.unmei.data.repository.NotificationRepositoryImpl
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.RoomDetail
import com.example.unmei.domain.repository.NotificationRepository

class NotifySendMessageUseCase(
    private val notificationRepository: NotificationRepository
) {

   suspend fun execute(
       roomDetail: RoomDetail,
       message: Message,//maybe String
       notificationRecipientsId:List<String>
   ){
       if(notificationRecipientsId.isEmpty())
           return
       notificationRepository.notifySendMessageInRooms(
           roomDetail = roomDetail,
           notificationRecipientsId = notificationRecipientsId,
           message=message
       )
   }
}