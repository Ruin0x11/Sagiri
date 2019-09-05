package xyz.ruin.sagiri.android

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import xyz.ruin.sagiri.AccountMatcher
import xyz.ruin.sagiri.MainActivity
import xyz.ruin.sagiri.booru.IBooruClient

class SagiriIntentService(private val client: IBooruClient) : IntentService("SagiriIntentService") {
    var notificationId = 1

    override fun onHandleIntent(intent: Intent) {
        val operation = intent.getSerializableExtra("operation") as SagiriServiceOperation?
        val uri = intent.data ?: return

        val account = AccountMatcher(this.applicationContext).findAccount(uri) ?: return

        val message = when (operation) {
            SagiriServiceOperation.POST -> handlePost(intent)
            else -> ""
        }

        sendNotification(message)
    }

    private fun handlePost(intent: Intent): String {
        return ""
    }

    private fun sendNotification(message: String) {
        val noti = NotificationCompat.Builder(applicationContext, MainActivity.CHANNEL_ID)
            //.setSmallIcon(R.drawable.ic_launcher)
            .setWhen(System.currentTimeMillis())
            .setContentTitle("Service")
            .setContentText(message)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, noti.build())
        }
        notificationId++
    }
}