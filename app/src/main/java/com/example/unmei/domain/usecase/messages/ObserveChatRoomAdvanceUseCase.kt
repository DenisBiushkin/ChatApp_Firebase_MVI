package com.example.unmei.domain.usecase.messages

import com.example.unmei.data.model.ChatRoomAdvance
import com.example.unmei.domain.model.messages.ChatRoom
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow


//пилим
class ObserveChatRoomAdvanceUseCase (
    private val repository: MainRepository,
    ) {


    fun execute(roomId:String) : Flow<ChatRoomAdvance>{
        return repository.observeChatRoomAdvance(roomId)
    }
}