package com.example.unmei.domain.usecase

import com.example.unmei.data.model.ChatRoomResponse
import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.repository.MainRepository
import dagger.hilt.InstallIn
import kotlinx.coroutines.flow.Flow
import org.example.Mappers.base.Mapper
import javax.inject.Inject

class ObserveChatRoomUseCase (
    private val repository: MainRepository,
    ) {


    fun execute(roomId:String) : Flow<ChatRoom>{
        return repository.observeChatRoom(roomId)
    }
}