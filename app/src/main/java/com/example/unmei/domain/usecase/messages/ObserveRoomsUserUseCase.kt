package com.example.unmei.domain.usecase.messages

import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class ObserveRoomsUserUseCase(
    private val repository: MainRepository
) {


    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(userId:String): Flow<List<String>> {
        return repository.observeRoomsUser(userId).mapNotNull { it.rooms.keys.toList() }
    }


}