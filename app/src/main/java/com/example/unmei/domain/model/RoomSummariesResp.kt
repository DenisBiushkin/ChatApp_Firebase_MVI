package com.example.unmei.domain.model

data class RoomSummaries(
        val lastMessage: Message? = null,
        val unreadedCount : Map<String,Int> = emptyMap(),
        val typingUsersStatus: Set<String> = emptySet()
)