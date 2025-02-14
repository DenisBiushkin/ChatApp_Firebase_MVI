package com.example.unmei.data.model

import com.example.unmei.domain.model.Status
import com.example.unmei.domain.model.StatusUser

data class StatusUserResponse (
    val status:String = "offline",
    val lastSeen:Long = 0L
){
    fun toStatusUser():StatusUser{
      val stat= when(this.status){
              "offline" -> Status.OFFLINE
              "online" -> Status.ONLINE
              "recently"->Status.RECENTLY
           else -> Status.OFFLINE
      }
        return StatusUser(
            status = stat,
            lastSeen= lastSeen
        )
    }
}




