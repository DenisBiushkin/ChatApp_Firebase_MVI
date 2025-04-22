package com.example.unmei.domain.usecase.messages

import com.example.unmei.data.repository.MainRepositoryImpl
import com.example.unmei.domain.model.RoomSummaries
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class ObserveRoomSummariesUseCase(

    private val mainRepositoryImpl: MainRepository
) {

    fun execute(chatId:String): Flow<RoomSummaries>{
        return mainRepositoryImpl.observeRoomSummaries(chatId)
    }
}