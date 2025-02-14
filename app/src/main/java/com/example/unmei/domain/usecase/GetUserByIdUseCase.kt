package com.example.unmei.domain.usecase

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource

class GetUserByIdUseCase(
    private val repository: MainRepository
) {

   suspend fun execute(userId:String) :User?{
       return repository.getUserById(userId)
   }
}