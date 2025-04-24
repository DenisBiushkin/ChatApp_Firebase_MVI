package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.messages.Message
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource

class SendMessageUseCaseById(
    private val repository: MainRepository
) {

    suspend fun execute(
        message: Message,
        chatId:String
    ):Resource<Unit>{
        if (chatId.isEmpty()){
            return Resource.Error(message="Пустое chatId")
        }
       return repository.sendMessageAdv(message,chatId)
    }
}