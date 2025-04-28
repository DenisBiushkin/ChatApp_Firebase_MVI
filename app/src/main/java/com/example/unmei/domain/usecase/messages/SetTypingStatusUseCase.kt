package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.model.TypingStatus
import com.example.unmei.domain.repository.MainRepository

class SetTypingStatusUseCase(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(groupId:String,userId: String,status: TypingStatus){
        mainRepository.updateStatusUserById(groupId,userId,status)
    }
}