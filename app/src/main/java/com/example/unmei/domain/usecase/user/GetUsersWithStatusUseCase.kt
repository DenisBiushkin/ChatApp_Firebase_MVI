package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.MainRepository

class GetUsersWithStatusUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke(userIds: List<String>): List<UserExtended> {
        return repository.getUsersWithStatus(userIds) ?: emptyList()
    }
}