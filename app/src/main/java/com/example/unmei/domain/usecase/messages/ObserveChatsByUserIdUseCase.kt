package com.example.unmei.domain.usecase.messages

import android.content.ContentValues.TAG
import android.util.Log
import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.model.TypeRoom
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.presentation.chat_list_feature.model.ChatItemAdvenced
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


class ObserveChatsByUserIdUseCase(

    private val repository: MainRepository,
    private val remote: RemoteDataSource
){

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(currentUserId: String): Flow<List<ChatItemAdvenced>> {
        return repository.observeRoomsUser(userId = currentUserId)
            .flatMapLatest { listRooms ->
                if (listRooms.rooms.isNullOrEmpty()) {
                    flowOf(emptyList())
                } else {
                    val chatFlows = listRooms.rooms.mapNotNull { chatId ->
                        remote.getChatRoomById(roomId = chatId)?.let { chatRoom ->
                            val summariesFlow = remote.observeRoomSammaries(chatRoom.id)
                            var newChatRoom = chatRoom

                            val statusFlow = if (chatRoom.type == TypeRoom.PRIVATE) {
                                val idCompanion = chatRoom.members.first { it != currentUserId }
                                val userData = repository.getUserById(idCompanion)
                                Log.d(TAG, "----USerDATA---- $userData")
                                userData?.let {
                                    newChatRoom = chatRoom.copy(
                                        chatName = it.fullName,
                                        iconUrl = it.photoUrl
                                    )
                                }
                                repository.observeStatusUserById(idCompanion)
                            } else {
                                flowOf(StatusUser(status = Status.OFFLINE, lastSeen = 0L))
                            }

                            combine(statusFlow, summariesFlow) { status, summaries ->
                                ChatItemAdvenced(
                                    chatRoom = newChatRoom,
                                    status = status,
                                    summaries = summaries
                                )
                            }
                        }
                    }

                    if (chatFlows.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        combine(chatFlows) { it.toList() }
                    }
                }
            }
    }



}

