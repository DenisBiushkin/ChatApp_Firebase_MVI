package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.TypingStatus
import com.example.unmei.domain.model.UserActivity
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class EnterChatUseCase(
    private val repository: MainRepository
) {

    suspend fun execute(chatId: String, userId: String)= coroutineScope{
        launch { repository.resetUnreadCountMessage(chatId,userId)}
        launch {  repository.updateActiveUserInRoomRemote(chatId,userId,UserActivity.ACTIVE)}
        launch {   repository.updateStatusUserById(
            groupId = chatId,
            userId = userId,
            status = TypingStatus.NONE
        )}
    }
}