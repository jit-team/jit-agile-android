package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.NotificationRepository

class FirebaseNotificationRepository(val dispatcher: CoroutineDispatcher) : NotificationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getDeviceToken(): Response<String?> {
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

    override suspend fun assignDeviceTokenToUser(userId: String, token: String): Response<Unit> {
        return retryWhenError {
            CoroutineScope(dispatcher).async {
                try {
                    val tokenMap = mapOf("token" to token)
                    firestore.collection("fcm_tokens")
                        .document(userId)
                        .set(tokenMap, SetOptions.merge())
                    response(Unit)
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFirestoreException<Unit>(e)
                }
            }.await()
        }
    }
}