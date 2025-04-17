package com.example.unmei.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.unmei.util.ConstansApp.MESSAGE_REC_KEY
import com.example.unmei.util.ConstansDev.TAG

class MessageReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,"Working (MessageReceiver)")
        val myFcmMessage = intent?.getStringExtra(MESSAGE_REC_KEY) ?: return
        Log.d(TAG,"Received Notification(MessageReceiver): "+myFcmMessage)
    }
}