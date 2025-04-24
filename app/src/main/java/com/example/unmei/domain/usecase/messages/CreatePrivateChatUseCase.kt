package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.model.messages.NewRoomModel
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
        message: Message
    ):Resource<String> {
        if (chatName.isEmpty())
            return Resource.Error(message = "Имя чата не должно быть пустым")
        if (membersIds.size != 2)
            return Resource.Error(message = "В приватном чате может быть только 2 собеседника")
        if (membersIds.first() ==membersIds[1])
            return Resource.Error(message = "Uid участников должно быть разным!")

        if (iconUrl.isEmpty())
            return Resource.Error(message = "Недопускается создание без иконки!")

        return repository.createNewChatAdvence(newRoomModel = NewRoomModel(
            chatName = chatName,
            iconUrl = iconUrl,
            type = TypeRoom.PRIVATE,
            membersIds = membersIds,
            message = message
           )
        )
    }
}