package com.example.unmei.domain.model

data class StatusUser(
    val status: Status,
    val lastSeen:Long
)

enum class Status{
    OFFLINE,
    ONLINE,
    RECENTLY
}
