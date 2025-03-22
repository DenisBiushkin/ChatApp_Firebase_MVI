package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class ObserveChatRoomUseCase (
    private val repository: MainRepository,
    ) {


    fun execute(roomId:String) : Flow<ChatRoom>{
        return repository.observeChatRoom(roomId)
    }
}