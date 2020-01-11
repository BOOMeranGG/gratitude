package com.orange_infinity.gratitude

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orange_infinity.gratitude.view.activity.MainActivity


private const val NOTIFY_ID = 101
private const val CHANNEL_ID = "Push channel"

class PushReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(TAG, "Start to getting message")

        if (message.notification != null) {
            Log.i(
                TAG, "Message notification title: \"${message.notification?.title}\"" +
                    " and body: \"${message.notification?.body}\""
            )
            createPushNotification(message.notification?.title ?: "", message.notification?.body ?: "")
        }
    }

    private fun createPushNotification(title: String, description: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Если использовать NotificationCompat.Builder(this, CHANNEL_ID) , то почему-то не меняются цвета
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher_background)
            //.setLargeIcon(largeIcon)
            .setColor(Color.parseColor("#4B8A08"))
            .setStyle(NotificationCompat.BigTextStyle().bigText(description))
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            //.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.circles0))
            //.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) // doesn't work, why?
            .setLights(Color.MAGENTA, 500, 1000)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build())
    }
}
