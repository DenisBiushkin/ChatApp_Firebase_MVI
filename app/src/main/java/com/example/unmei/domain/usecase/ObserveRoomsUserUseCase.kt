package com.example.unmei.domain.usecase

import com.example.unmei.domain.model.ChatRoom
import com.example.unmei.domain.model.RoomsUser
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull

class ObserveRoomsUserUseCase(
    private val repository: MainRepository
) {


    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(userId:String): Flow<List<String>> {
        return repository.observeRoomsUser(userId).mapNotNull { it.rooms.keys.toList() }
    }


}