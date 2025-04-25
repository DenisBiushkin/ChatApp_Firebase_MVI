package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UpdateProfileUserNameById(
    private val mainRepository: MainRepository
) {

    suspend fun execute(
        userId:String,
        newUserName:String
    ):Resource<Unit> = coroutineScope{

        val usernameExistsDeferred = async { mainRepository.getExistenceUsername(newUserName) }
        val userDeferred = async { mainRepository.getUserById(userId) }
        if (usernameExistsDeferred.await()) {
            return@coroutineScope Resource.Error(message = "Такой username уже существует")
        }
        val user = userDeferred.await()?: return@coroutineScope Resource.Error(message = "Неудалось сохранить UserName")
        if (user.userName==newUserName){
            return@coroutineScope Resource.Error(message = "Чтобы редактировать, он должен отличаться от старого")
        }
        val result=mainRepository.updateUsernameInProfile(userId, newUserName = newUserName, oldUserName = user.userName)
        if (result)
            return@coroutineScope Resource.Success(Unit)

        return@coroutineScope Resource.Error(message ="Ошибка сохранения")
    }
}