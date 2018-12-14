package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.repository.NotificationRepository

class FirebaseNotificationRepository(
    private val firestore: FirebaseFirestore,
    dispatcher: CoroutineDispatcher
) : NotificationRepository {
    private val scope = CoroutineScope(dispatcher)

    override suspend fun assignDeviceTokenToUser(userId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val tokenResponse = getDeviceToken()
                    when (tokenResponse) {
                        is Success -> handleSuccess(userId, tokenResponse.data)
                        is Failure -> Failure(tokenResponse.error)
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
            Success(Unit)
        } else {
            FirebaseErrorResolver.parseFirestoreException(task.exception ?: Exception())
        }
    }

    private suspend fun getDeviceToken(): Response<String> {
        return retryWhenError {
            scope.async {
                try {
                    val task = FirebaseInstanceId.getInstance().instanceId
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(task.result!!.token)
                    } else {
                        FirebaseErrorResolver.parseInstanceException<String>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseInstanceException<String>(e)
                }
            }.await()
        }
    }

    override suspend fun unassignDeviceTokenFromUser(userId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = firestore.collection("fcm_tokens")
                        .document(userId)
                        .delete()
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFirestoreException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFirestoreException<Unit>(e)
                }
            }.await()
        }
    }
}