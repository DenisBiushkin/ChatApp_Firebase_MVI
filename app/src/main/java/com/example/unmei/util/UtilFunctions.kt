package com.example.unmei.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale


fun timestampToStringHourMinute(timestamp: Long):String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(timestamp))
}
fun toStringTime(timeStamp:Long):String{
    //val timeStamp: Long =1737392296
    val formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
    val date = Instant.ofEpochMilli(timeStamp)
        .atZone(ZoneOffset.UTC) // Устанавливаем временную зону
        .toLocalDate() // Преобразуем в локальную дату
    val text = date.format(formatter)
    val russianDayOfWeek = date.month.getDisplayName(TextStyle.SHORT, Locale("ru"))
    return date.dayOfMonth.toString()+" "+russianDayOfWeek
}
fun timestampToLocalDateTime(timestamp:Long): LocalDateTime {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())  // Использует системный часовой пояс
        .toLocalDateTime()
}
fun getAdvancedStatusUser(timeStamp:Long):String{
    val now = LocalDateTime.now().toLocalDate()
    val date = Instant.ofEpochMilli(timeStamp)
        .atZone(ZoneOffset.UTC) // Устанавливаем временную зону
        .toLocalDate() // Преобразуем в локальную дату
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    if (now==date){
        return "был(а) в "+sdf.format(Date(timeStamp))
    }
    if(
        (now.year==date.year)&&(now.dayOfMonth==date.dayOfMonth+1)
    ){
        return "был(а) вчера в "+sdf.format(Date(timeStamp))
    }
    val russianDayOfWeek = date.month.getDisplayName(TextStyle.SHORT, Locale("ru"))
    return "был(а) "+date.dayOfMonth.toString()+" "+russianDayOfWeek+" в "+sdf.format(Date(timeStamp))

}