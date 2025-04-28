package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.TypingStatus
import com.example.unmei.domain.model.UserActivity
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LeftChatUseCase(
    private val repository: MainRepository
) {
    suspend fun execute(chatId: String, userId: String)= coroutineScope{
        launch {   repository.updateActiveUserInRoomRemote(chatId,userId, UserActivity.INACTIVE)}
       launch {   repository.updateStatusUserById(
            groupId = chatId,
            userId = userId,
            status = TypingStatus.NONE
        )}
    }
}