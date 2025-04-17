package com.example.unmei.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.unmei.R
import com.example.unmei.receiver.ReadedMessageReceiver
import com.example.unmei.receiver.ReplyMessageReceiver
import com.example.unmei.util.ConstansApp.KEY_TEXT_REPLY
import com.example.unmei.util.ConstansApp.MESSAGE_CHANNEL_NAME
import com.example.unmei.util.ConstansApp.NEW_MESSAGE_CHANNEL_ID

import kotlin.math.min


class MessageNotificationHelper (
    private val context: Context
){



    fun getCircularBitmapIcon(context: Context, @DrawableRes resId: Int): IconCompat {
        val srcBitmap = BitmapFactory.decodeResource(context.resources, resId)
        val size = min(srcBitmap.width, srcBitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)

        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(srcBitmap, null, rect, paint)

        return IconCompat.createWithBitmap(output)
    }




    fun createNotificationWithReply(
        channelId:String,
        title:String,
        body:String,
        icon:IconCompat

    ): NotificationCompat.Builder {
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE
            } else
                0
        val replyIntent = Intent(context, ReplyMessageReceiver::class.java)
        val readedIntent =Intent(context, ReadedMessageReceiver::class.java)

        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel("Ответить")
            .build()

        val replyPendingIntent = PendingIntent.getBroadcast(
            context, 0,
            replyIntent,
            flag)


        val readedPendingntent = PendingIntent.getBroadcast(
            context,
            12,
            readedIntent,
            flag)

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




        val user = Person.Builder()
            .setName(title)
            .setImportant(true)
            .setIcon(icon)
            .build()
        val style = NotificationCompat.MessagingStyle(user)
            .setConversationTitle(title)
            .setGroupConversation(false)
           .addMessage(body, System.currentTimeMillis(), user)

        return NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
             .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .addAction(replyAction)
            .addAction(readedAction)
            .setColor(0x0000FF)
            .setStyle(style)
    }
    suspend fun loadBitmapFromUrl(context: Context, imageUrl: String): IconCompat? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Важно: нужен software Bitmap
            .build()

        val result = loader.execute(request)
        val bitmap=(result.drawable as? BitmapDrawable)?.bitmap ?: return null
        return  IconCompat.createWithBitmap( bitmap)
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
}