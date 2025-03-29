package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository

class GetUserByIdUseCase(
    private val repository: MainRepository
) {

   suspend fun execute(userId:String) :User?{
       return repository.getUserById(userId)
   }
}