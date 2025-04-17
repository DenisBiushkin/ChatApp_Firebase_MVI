package com.example.unmei.service

import android.util.Log
import android.content.Intent
import com.example.unmei.data.model.toMyFcmData
import com.example.unmei.receiver.MessageReceiver
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService(

):FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"Token FCM: $token")
        //update server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)




        val intent = Intent(this,MessageReceiver::class.java)
        val data = message.data

        Log.d(TAG,"MyFcmData "+message.toMyFcmData())


        Log.d(TAG,"Message Received: ${message.notification?.title.toString()}")
        sendBroadcast(intent)


        //переопределть service
    }
}