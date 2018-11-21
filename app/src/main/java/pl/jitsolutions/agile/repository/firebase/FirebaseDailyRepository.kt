package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.DailyRepository
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseDailyRepository(private val dispatcher: CoroutineDispatcher) : DailyRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val functions = FirebaseFunctions.getInstance()
    private lateinit var observeDailyListener: ListenerRegistration

    override suspend fun endDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("finishDaily")
                    .call(endDailyParams(dailyId))
                    .addOnSuccessListener { continuation.resume(response(Unit)) }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun observeDaily(dailyId: String): ReceiveChannel<Response<Daily?>> {
        val channel = Channel<Response<Daily?>>()
        observeDailyListener = firestore.collection("dailies")
            .document(dailyId)
            .addSnapshotListener { document, exception ->
                CoroutineScope(dispatcher).launch {
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
                response(daily)
            }
            else -> response(null)
        }
    }

    override suspend fun nextDailyUser(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("nextDailyUser")
                    .call(nextDailyUserParams(dailyId))
                    .addOnSuccessListener { continuation.resume(response(Unit)) }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun joinDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("joinDaily")
                    .call(joinDailyParams(dailyId))
                    .addOnSuccessListener { continuation.resume(response(Unit)) }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun leaveDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("leaveDaily")
                    .call(leaveDailyParams(dailyId))
                    .addOnSuccessListener { continuation.resume(response(Unit)) }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override suspend fun startDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                functions
                    .getHttpsCallable("startDaily")
                    .call(startDailyParams(dailyId))
                    .addOnSuccessListener { continuation.resume(response(Unit)) }
                    .addOnFailureListener {
                        continuation.resume(FirebaseErrorResolver.parseFunctionException(it))
                    }
            }
        }.await()
    }

    override fun dispose() {
        observeDailyListener.remove()
    }
}
