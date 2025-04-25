package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UpdateProfileFullNameById(
    private val mainRepository: MainRepository
) {

    suspend fun execute(
        userId:String,
        newFullName:String
    ): Resource<Unit> = coroutineScope{

        val userDeferred = async { mainRepository.getUserById(userId) }
        val user = userDeferred.await()?: return@coroutineScope Resource.Error(message = "Неудалось сохранить FullName")
        if (user.fullName==newFullName){
            return@coroutineScope Resource.Error(message = "Чтобы редактировать, он должен отличаться от старого")
        }
        val result=mainRepository.updateFullNameInProfileRemote(
            userId=userId,
            oldFullName = user.fullName,
            newFullName =newFullName
        )
        if (result)
            return@coroutineScope Resource.Success(Unit)

        return@coroutineScope Resource.Error(message ="Ошибка сохранения")
    }
}