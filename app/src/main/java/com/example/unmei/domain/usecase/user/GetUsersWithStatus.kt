package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.MainRepository

class GetUsersWithStatus(
    private val mainRepository: MainRepository
) {

    suspend fun execute(userIds:List<String>):List<UserExtended>?{
        return mainRepository.getUsersWithStatus(userIds)
    }
}