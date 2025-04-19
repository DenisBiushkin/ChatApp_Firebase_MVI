package com.example.unmei.android_frameworks.service

import android.util.Log
import android.content.Intent
import com.example.unmei.data.model.FcmMessage
import com.example.unmei.data.model.NtfMessage
import com.example.unmei.data.model.toMyFcmData
import com.example.unmei.android_frameworks.receiver.MessageReceiver
import com.example.unmei.util.ConstansApp.MESSAGE_REC_KEY
import com.example.unmei.util.ConstansDev.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService(

):FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"Token FCM: $token")


        //update server
        //saveTokenServer(token:String) suspend
    }

    private fun getMyFcmMessage(message: RemoteMessage):FcmMessage{
        val myFcmData=message.toMyFcmData()
        return FcmMessage(
            message = NtfMessage(
                notification =null,//исключаем использование стандартного уведомления
                data = myFcmData
            )
        )
    }
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG,"onMessageReceived (PushNotificationService)")
        val myfcmMessage=getMyFcmMessage(message)
        val intent = Intent(this, MessageReceiver::class.java).apply {
            putExtra(MESSAGE_REC_KEY,myfcmMessage.toJson())
        }
//        Log.d(TAG,"MyFcmData "+message.toMyFcmData())
//        Log.d(TAG,"Message Received: ${message.notification?.title.toString()}")
        sendBroadcast(intent)
    }
}