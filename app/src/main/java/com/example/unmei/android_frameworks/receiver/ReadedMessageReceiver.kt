package com.example.unmei.android_frameworks.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.unmei.android_frameworks.receiver.model.NotificationDataExtra
import com.example.unmei.util.ConstansApp.NOTIFICATION_ID_PENDING_EXTRAS
import com.example.unmei.android_frameworks.notification.MessageNotificationHelper
import com.example.unmei.util.ConstansDev.TAG

class ReadedMessageReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val safeContext = context ?: return
        val json = intent?.getStringExtra(NOTIFICATION_ID_PENDING_EXTRAS) ?: return

        val data = NotificationDataExtra.fromJson(json)
        val helper= MessageNotificationHelper(safeContext)


        //отметить как прочимтанное UseCase

        Log.d(TAG,"ReadedMessageReceiver")
        NotificationManagerCompat.from(context).cancel(data.notificationId)
    }
}