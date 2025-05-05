package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext

class GetUserWithStatusUseCase(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(
        userId:String
    ):UserExtended? {
        //нет времени чтобы дописать нормально
        val result=mainRepository.getUsersWithStatus(listOf(userId))
        if (result!=null){
            if (!result.isEmpty()){
                return result.first()
            }
        }
        return null
    }
}