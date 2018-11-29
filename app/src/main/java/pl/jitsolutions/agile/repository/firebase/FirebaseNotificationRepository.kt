package pl.jitsolutions.agile.repository.firebase

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.isSuccessfulWithData
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.presentation.MainActivity
import pl.jitsolutions.agile.repository.NotificationRepository

class FirebaseNotificationRepository(
    private val application: Application,
    val dispatcher: CoroutineDispatcher
) : NotificationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun assignDeviceTokenToUser(userId: String): Response<Unit> {
        return retryWhenError {
            CoroutineScope(dispatcher).async {
                try {
                    val tokenResponse = getDeviceToken()
                    if (tokenResponse.isSuccessfulWithData()) {
                        handleSuccess(userId, tokenResponse.data!!)
                    } else {
                        errorResponse(error = tokenResponse.error ?: Error.Unknown)
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFirestoreException<Unit>(e)
                }
            }.await()
        }
    }

    private fun handleSuccess(userId: String, token: String): Response<Unit> {
        val tokenMap = mapOf("token" to token)
        val task = firestore.collection("fcm_tokens")
            .document(userId)
            .set(tokenMap, SetOptions.merge())
        Tasks.await(task)
        return if (task.isSuccessful) {
            response(Unit)
        } else {
            FirebaseErrorResolver.parseFirestoreException(task.exception ?: Exception())
        }
    }

    private suspend fun getDeviceToken(): Response<String?> {
        return retryWhenError {
            CoroutineScope(dispatcher).async {
                try {
                    val task = FirebaseInstanceId.getInstance().instanceId
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        response(task.result?.token)
                    } else {
                        FirebaseErrorResolver.parseInstanceException(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseInstanceException<String?>(e)
                }
            }.await()
        }
    }

    override suspend fun showNotification(projectName: String) {
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