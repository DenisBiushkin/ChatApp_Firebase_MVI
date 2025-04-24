package com.example.unmei.domain.model.messages

data class RoomSummaries(
    val lastMessage: Message? = null,
    val unreadedCount : Map<String,Int> = emptyMap(),
    val typingUsersStatus: Set<String> = emptySet()
)