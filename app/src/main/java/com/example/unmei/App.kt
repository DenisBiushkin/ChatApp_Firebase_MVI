package com.example.unmei

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.example.unmei.android_frameworks.notification.MessageNotificationHelper
import com.example.unmei.util.ConstansApp.NOTIFICATION_TOKENS_DB
import com.example.unmei.util.ConstansDev.TAG
import com.example.unmei.util.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.tasks.await

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
     //   FirebaseApp.initializeApp(this)
//        MessageNotificationHelper.init(this)
//        MessageNotificationHelper.get().createNotificationChannel()
    }
}