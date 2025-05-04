package com.example.unmei.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun timestampToStringHourMinute(timestamp: Long):String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(timestamp))
}
fun timestampToLocalDateTime(timestamp:Long): LocalDateTime {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())  // Использует системный часовой пояс
        .toLocalDateTime()
}