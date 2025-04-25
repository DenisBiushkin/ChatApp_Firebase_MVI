package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.MainRepository

class GetFriendsByUserId(
    private val mainRepository: MainRepository
) {
    suspend fun execute(userId: String):List<UserExtended>? {
        return mainRepository.getFriendsByUserId(userId)
    }
}