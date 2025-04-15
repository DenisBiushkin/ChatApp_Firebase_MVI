package com.example.unmei.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.unmei.util.MessageNotificationHelper
import com.example.unmei.util.ConstansDev.TAG

class ReadedMessageReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val currentContext = context!!.applicationContext
        val helper = MessageNotificationHelper(context)
        Log.d(TAG,"ReadedMessageReceiver")
        NotificationManagerCompat.from(context).cancel(2)
    }
}