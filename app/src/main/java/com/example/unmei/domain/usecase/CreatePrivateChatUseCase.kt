package com.example.unmei.domain.usecase

import android.net.Uri
import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.NewRoomModel
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource

class CreatePrivateChatUseCase(
    private val repository: MainRepository
) {
    suspend fun execute(
        chatName:String,
        iconUrl:String,
        membersIds:List<String>,
        message: Message =Message(senderId = "")
    ):Resource<String> {
        if (chatName.isEmpty())
            return Resource.Error(message = "Имя чата не должно быть пустым")
        if (membersIds.size != 2)
            return Resource.Error(message = "В приватном чате может быть только 2 собеседника")
        if (iconUrl.isEmpty())
            return Resource.Error(message = "Недопускается создание без иконки!")
        return repository.createNewChatAdvence(newRoomModel = NewRoomModel(
            chatName = chatName,
            iconUrl = iconUrl,
            type = TypeRoom.PRIVATE,
            membersIds = membersIds
           )
        )
    }
}