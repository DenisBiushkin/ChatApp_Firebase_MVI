package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SendMessageUseCaseById(
    private val repository: MainRepository,
    private val notificationRepository: NotificationRepository
) {

    suspend fun execute(
        message: Message,
        chatId:String,

        //For quick((
        offlineUsersIds:List<String>,
        prevUnreadCount:Map<String,Int>,
        roomDetail:RoomDetail
    ):Resource<Unit> = coroutineScope{
        if (chatId.isEmpty()){
            return@coroutineScope Resource.Error(message="Пустое chatId")
        }
        if (offlineUsersIds.isEmpty())
            return@coroutineScope repository.sendMessageAdv(message,chatId)

        val newMapUnread = offlineUsersIds.map {
            it to (prevUnreadCount[it]?.inc() ?: 1)
        }.toMap()

        val sendMessageResult =async {   repository.sendMessageAdv(message,chatId)}



        val notifResult=async {  notificationRepository.notifySendMessageInRooms(roomDetail = roomDetail,message,offlineUsersIds)}
        val summerisRusult =async { repository.updateUnreadCountInRoomSummaries(roomId = chatId, newUnreadCount = newMapUnread)}
        notifResult.await()
        if (
            (sendMessageResult.await() is Resource.Success)
            &&
            summerisRusult.await()
        ){
            return@coroutineScope  Resource.Success(Unit)
        }
        return@coroutineScope  Resource.Error(message="Ошибка отправки")
    }
}