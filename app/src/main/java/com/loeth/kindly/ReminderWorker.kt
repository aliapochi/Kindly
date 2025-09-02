package com.loeth.kindly

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar

class ReminderWorker(
    context: Context,
    workParam: WorkerParameters,
) : Worker(context, workParam) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        val channelName = "Reminder Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // ✅ Required for API 31+
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.kindly_logo)
            .setContentTitle("Well Done!")
            .setContentText("Great job adding a promise! Stay committed!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        sendDailyNotification(context)
    }

    private fun sendDailyNotification(context: Context) {
        val messages = listOf(
            "A promise made is a step toward growth. Keep going!",
            "Your promise isn’t just words—it’s a commitment to yourself.",
            "Small actions today bring big results tomorrow. Honor your promise!",
            "Stay true to your word. A fulfilled promise is a fulfilled dream.",
            "You set this goal for a reason. Don’t let yourself down!",
            "Promises are like seeds—nurture them, and they’ll grow into success.",
            "Every fulfilled promise builds your self-trust. Keep at it!",
            "Consistency turns promises into achievements.",
            "Your future self is counting on you. Stay committed!",
            "One step closer to keeping your promise. Keep pushing!"
        )

        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lastIndex = sharedPreferences.getInt("last_message_index", -1)

        // Get the next message index
        val nextIndex = (lastIndex + 1) % messages.size
        val message = messages[nextIndex]

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_message_channel"
        val channelName = "Daily Motivation"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }



        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.kindly_logo)
            .setContentTitle("Daily Inspiration 🌟")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notification) // Unique ID for each notification

        // Save the new index
        sharedPreferences.edit().putInt("last_message_index", nextIndex).apply()
    }
}

fun scheduleDailyMessage(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Set alarm to repeat daily at a fixed time (e.g., 9:00 AM)
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 9)  // Change to your preferred time
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    if (calendar.timeInMillis < System.currentTimeMillis()) {
        // If time has already passed for today, schedule for tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY, // Repeat every day
        pendingIntent
    )
}

class ReminderDueDate(
    context: Context,
    workParam: WorkerParameters,
) : Worker(context, workParam) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        val channelName = "Reminder Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // ✅ Required for API 31+
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.kindly_logo)
            .setContentTitle("Don't Miss Your Promise!")
            .setContentText("You have a promise due in 24 hours")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}
