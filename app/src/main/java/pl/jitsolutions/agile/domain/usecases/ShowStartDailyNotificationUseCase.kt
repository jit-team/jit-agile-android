package pl.jitsolutions.agile.domain.usecases

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.MainActivity
import pl.jitsolutions.agile.repository.SystemInfoRepository

class ShowStartDailyNotificationUseCase(
    private val systemInfoRepository: SystemInfoRepository,
    private val application: Application
) {

    fun showNotification(projectName: String) {
        if (systemInfoRepository.getApplicationState().data == SystemInfoRepository.AppState.BACKGROUND) {
            notification(projectName)
        }
    }

    private fun notification(projectName: String) {
        val intent = Intent(application, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            application, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(application, "")
            .setSmallIcon(R.drawable.ic_daily_24dp_white)
            .setContentTitle(projectName)
            .setContentText(application.getString(R.string.notification_daily_started))
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setChannelId(application.getString(R.string.notification_channel_id))
            .setContentIntent(pendingIntent)

        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                application.getString(R.string.notification_channel_id),
                application.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, builder.build())
    }
}
