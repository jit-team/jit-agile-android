package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.repository.DailyRepository

class FirebaseDailyRepository(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions,
    dispatcher: CoroutineDispatcher
) : DailyRepository {
    private lateinit var observeDailyListener: ListenerRegistration
    private val scope = CoroutineScope(dispatcher)

    override suspend fun endDaily(dailyId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("finishDaily")
                        .call(endDailyParams(dailyId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun observeDaily(dailyId: String): ReceiveChannel<Response<Daily?>> {
        val channel = Channel<Response<Daily?>>()
        observeDailyListener = firestore.collection("dailies")
            .document(dailyId)
            .addSnapshotListener { document, exception ->
                scope.launch {
                    when {
                        document != null -> {
                            channel.send(handleDailyDocument(document))
                        }
                        exception != null -> channel.send(
                            FirebaseErrorResolver.parseFunctionException(exception)
                        )
                    }
                }
            }
        return channel
    }

    private fun handleDailyDocument(document: DocumentSnapshot): Response<Daily?> {
        return when {
            document.data != null -> {
                val daily = document.data.toDaily()
                Success(daily)
            }
            else -> Success(null)
        }
    }

    override suspend fun nextDailyUser(dailyId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("nextDailyUser")
                        .call(nextDailyUserParams(dailyId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun joinDaily(dailyId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("joinDaily")
                        .call(joinDailyParams(dailyId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun leaveDaily(dailyId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("leaveDaily")
                        .call(leaveDailyParams(dailyId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun startDaily(dailyId: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = functions
                        .getHttpsCallable("startDaily")
                        .call(startDailyParams(dailyId))
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseFunctionException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseFunctionException<Unit>(e)
                }
            }.await()
        }
    }

    override fun dispose() {
        observeDailyListener.remove()
    }
}
