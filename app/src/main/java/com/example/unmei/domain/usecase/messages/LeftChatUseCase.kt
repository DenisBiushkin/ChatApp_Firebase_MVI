package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.UserActivity
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LeftChatUseCase(
    private val repository: MainRepository
) {
    suspend fun execute(chatId: String, userId: String){
       repository.updateActiveUserInRoomRemote(chatId,userId, UserActivity.INACTIVE)
    }
}