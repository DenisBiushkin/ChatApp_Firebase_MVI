package com.example.unmei.domain.usecase.messages

import android.net.Uri
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.NewRoomModel
import com.example.unmei.domain.model.messages.RoomDetail
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.domain.repository.NotificationRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CreatePublicChatUseCase(

    private val repository: MainRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend fun execute(
        chatName:String,
        iconUri: Uri,
        membersIds:List<String>,
        moderatorsIds:List<String>,
    ): Resource<String> = coroutineScope{

        if (chatName.isEmpty())
            return@coroutineScope Resource.Error(message = "Имя чата не должно быть пустым")
        if (membersIds.size < 2)
            return@coroutineScope Resource.Error(message = "В приватном чате может быть только 2 собеседника")
        if (moderatorsIds.isEmpty())
            return@coroutineScope Resource.Error(message = "В группе должен быть хотя бы 1 модератор")

//        if (iconUrl.isEmpty())
//            return Resource.Error(message = "Недопускается создание без иконки!")
        val senderId = membersIds.first()
        val message = Message(
            senderId= senderId,
            text = "Была создана новая группа"
        )
        val createResult = async { repository.createNewChatAdvence(newRoomModel = NewRoomModel(
            chatName = chatName,
            type = TypeRoom.PUBLIC,
            membersIds = membersIds,
            moderatorsIds = moderatorsIds,
            message = message,
            iconUri = iconUri
        ) )}.await()
//        val notifResult =async {
//            notificationRepository.notifySendMessageInRooms(
//            roomDetail = RoomDetail()
//            )
//        }.await()

        if (
            createResult is Resource.Success
            ){
            return@coroutineScope Resource.Success(createResult.data)
        }else
        return@coroutineScope Resource.Error(message=createResult.message?:"Ошибка создания")
    }
}