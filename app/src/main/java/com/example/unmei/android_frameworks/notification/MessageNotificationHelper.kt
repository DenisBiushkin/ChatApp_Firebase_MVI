package com.example.unmei.android_frameworks.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent


import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.unmei.MainActivity
import com.example.unmei.R
import com.example.unmei.android_frameworks.receiver.model.NotificationDataExtra
import com.example.unmei.android_frameworks.receiver.ReadedMessageReceiver
import com.example.unmei.android_frameworks.receiver.ReplyMessageReceiver
import com.example.unmei.data.model.FcmData
import com.example.unmei.presentation.util.model.NavigateConversationData
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansApp.KEY_TEXT_REPLY
import com.example.unmei.util.ConstansApp.MESSAGE_CHANNEL_NAME
import com.example.unmei.util.ConstansApp.NEW_MESSAGE_CHANNEL_ID
import com.example.unmei.util.ConstansApp.NOTIFICATION_ID_PENDING_EXTRAS


class MessageNotificationHelper (
    private val context: Context
){

    private fun getMessagesStyle(
        title: String,
        enableGroupConversation:Boolean=false,
        icon: IconCompat,
    ): NotificationCompat.MessagingStyle {
        val user = Person.Builder()
            .setName(title)
            .setImportant(true)
            .setIcon(icon)
            .build()
        return NotificationCompat.MessagingStyle(user)
            // .setConversationTitle("Cha He In")
            .setGroupConversation(enableGroupConversation)
    }
    private fun getMessageStyleActions(
        notificationId: Int,
        roomId:String,
    ): Pair<NotificationCompat.Action, NotificationCompat.Action> {
        val replyIntent = Intent(context, ReplyMessageReceiver::class.java).apply {
            putExtra(
                NOTIFICATION_ID_PENDING_EXTRAS, NotificationDataExtra(
                    notificationId =notificationId,
                    roomId = roomId
                ).toJson()
            )

        }
        val readedIntent =Intent(context, ReadedMessageReceiver::class.java).apply {
            putExtra(
                NOTIFICATION_ID_PENDING_EXTRAS, NotificationDataExtra(
                    notificationId =notificationId,
                    roomId = null
                ).toJson()
            )
        }

        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel("Ответить")
            .build()

        val replyPendingIntent = PendingIntent.getBroadcast(
            context, notificationId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)


        val readedPendingntent = PendingIntent.getBroadcast(
            context,
            notificationId+1,
            readedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        val replyAction = NotificationCompat.Action.Builder(
            0,
            "Ответить",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val readedAction = NotificationCompat.Action.Builder(
            1,
            "Прочитано",
            readedPendingntent
        ).build()
        return Pair(replyAction,readedAction)
    }

    suspend fun createSingleMessageNotification(
        notificationId: Int,
        data:FcmData
    ): NotificationCompat.Builder {
        //чекнуть в Room есть ли уведомление по id мб
        val icon= loadBitmapFromUrl(data.image)
        val style = getMessagesStyle(
            title = data.title,
            icon=icon,
            enableGroupConversation = true
        )
        val user = Person.Builder().setIcon(icon).setName(data.title).build()
            var textMessage=data.body
            if(data.body.isEmpty()){
                textMessage="Прикрепелено вложение"
            }
            style.addMessage(
                textMessage,System.currentTimeMillis(),user
            )
        val dataDeepLink= NavigateConversationData(
            chatExist = false,
            chatUrl = data.image,
            chatName = data.title,
            companionUid="",
            chatUid = data.roomId
        ).toJson()
        val deepLinkPendingIntent= getDeeplinkPendingIntent(
            "${ConstansApp.CHAT_URI_DEEPLINK}/${dataDeepLink}"
        )

        return messageNotification(
            notificationId = notificationId,
            notifChannelId = ConstansApp.NEW_MESSAGE_CHANNEL_ID,
            roomId = data.roomId,
            groupId = ConstansApp.MESSAGE_GROUP_KEY,
            style = style
        ).setContentIntent(deepLinkPendingIntent)
    }

    private fun getDeeplinkPendingIntent(
        pathDeeplink:String
    ): PendingIntent {
        val deeplinkIntent = Intent(
            Intent.ACTION_VIEW,
            pathDeeplink.toUri(),
            context,
            MainActivity::class.java
        )
        val deeplinkPendingIntent :PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deeplinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)!!
        }
        return deeplinkPendingIntent
    }

    suspend private fun loadBitmapFromUrl(
        imageUrl: String,
        @DrawableRes indefiniteImage:Int = R.drawable.indefine
    ): IconCompat{
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Важно: нужен software Bitmap
            .build()

        return try {
            val result = loader.execute(request)

            val bitmap=(result.drawable as? BitmapDrawable)?.bitmap
            if(bitmap!=null)
                IconCompat.createWithBitmap( bitmap)
            else
                IconCompat.createWithResource(context,indefiniteImage)
        }catch (e:Exception){
            IconCompat.createWithResource(context,indefiniteImage)
        }
    }

    fun createNotificationChannel(
        channelName:String=MESSAGE_CHANNEL_NAME,
        channelId:String=NEW_MESSAGE_CHANNEL_ID,
        importance:Int =NotificationManager.IMPORTANCE_DEFAULT
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            ) .apply {
                //включить бейдж
               // setShowBadge(true)
            }
//            val notificationManager: NotificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun messageNotification(
        notifChannelId:String,
        notificationId: Int,
        roomId: String,
        groupId:String?=null,
        style:NotificationCompat.MessagingStyle

    ): NotificationCompat.Builder {
        val pairActions = getMessageStyleActions(
            notificationId = notificationId,
            roomId = roomId
        )
        return NotificationCompat
            .Builder(context,notifChannelId)
            .setSmallIcon(R.drawable.erishkagel)
            .setGroup(groupId)
            .setStyle(style)
          //  .setOnlyAlertOnce(true)
            .addAction(pairActions.first)
            .addAction(pairActions.second)

    }
    fun summaryGroupNotification(
        notifChannelId:String,
        groupId:String
    ): NotificationCompat.Builder {
        return NotificationCompat
            .Builder(context,notifChannelId)
            .setSmallIcon(R.drawable.erishkagel)
            .setGroup(groupId)
            .setGroupSummary(true)
    }


    fun isNotificationActive( id: Int): Boolean {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activeNotifications = manager.activeNotifications
        return activeNotifications.any { it.id == id }
    }
    fun getActiveNotificationsCount():Int{
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager.activeNotifications.size
    }


}