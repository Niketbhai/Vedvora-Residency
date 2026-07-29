package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity

object NotificationHelper {

    const val CHANNEL_ID = "vedvora_service_status_channel"
    private const val CHANNEL_NAME = "Service & Concierge Realtime Updates"
    private const val CHANNEL_DESC = "Push notification alerts for resident service request status changes (In Progress, Completed, Confirmed)"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showServiceStatusNotification(
        context: Context,
        serviceName: String,
        newStatus: String,
        additionalDetails: String = "",
        notificationId: Int = (System.currentTimeMillis() % 10000).toInt()
    ) {
        createNotificationChannel(context)

        // Check POST_NOTIFICATIONS permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // Cannot post notification without permission
                return
            }
        }

        val title = when (newStatus.lowercase()) {
            "in progress" -> "⚡ Service In Progress: $serviceName"
            "completed" -> "✅ Service Completed: $serviceName"
            "confirmed" -> "📌 Service Confirmed: $serviceName"
            "pending concierge" -> "⏳ Service Submitted: $serviceName"
            else -> "🛎️ Service Status Updated: $serviceName"
        }

        val statusText = when (newStatus.lowercase()) {
            "in progress" -> "Concierge staff or maintenance specialists are currently fulfilling your request."
            "completed" -> "Your service request has been completed successfully! Tap to view details or rate your experience."
            "confirmed" -> "Your requested time slot and concierge staff assignment have been verified."
            else -> "Status has been updated to $newStatus."
        }

        val message = if (additionalDetails.isNotBlank()) "$statusText ($additionalDetails)" else statusText

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}
