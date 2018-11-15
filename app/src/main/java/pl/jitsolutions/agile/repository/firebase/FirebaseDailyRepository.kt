package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.DailyRepository
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseDailyRepository(private val dispatcher: CoroutineDispatcher) : DailyRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val functions = FirebaseFunctions.getInstance()

    override suspend fun endDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to dailyId)
                functions
                    .getHttpsCallable("finishDaily")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = it))
                    }
            }
        }.await()
    }

    override suspend fun observeDaily(dailyId: String): ReceiveChannel<Response<Daily?>> {
        val channel = Channel<Response<Daily?>>()
        firestore.collection("dailies")
            .document(dailyId)
            .addSnapshotListener { document, exception ->
                CoroutineScope(dispatcher).launch {
                    when {
                        document != null -> {
                            channel.send(handleDailyDocument(document))
                        }
                        exception != null -> channel.send(errorResponse(error = Exception()))
                    }
                }
            }
        return channel
    }

    override suspend fun nextDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to dailyId)
                functions
                    .getHttpsCallable("nextDailyUser")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = it))
                    }
            }
        }.await()
    }
    private fun handleDailyDocument(document: DocumentSnapshot): Response<Daily?> {
        return when {
            document.data != null -> {
                val daily = document.data.toDomainDaily()
                response(daily)
            }
            else -> response(null)
        }
    }

    override suspend fun joinDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to dailyId)
                functions
                    .getHttpsCallable("joinDaily")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = it))
                    }
            }
        }.await()
    }

    override suspend fun leaveDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to dailyId)
                functions
                    .getHttpsCallable("leaveDaily")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(response(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = it))
                    }
            }
        }.await()
    }

    override suspend fun startDaily(dailyId: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                val data = mutableMapOf("projectId" to dailyId)
                functions
                    .getHttpsCallable("startDaily")
                    .call(data)
                    .addOnSuccessListener {
                        continuation.resume(
                            response(
                                Unit
                            )
                        )
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = it))
                    }
            }
        }.await()
    }

    @Suppress("UNCHECKED_CAST")
    private fun Any?.toDomainDaily(): Daily {
        val dailyMap = this as Map<String, Any>
        return Daily(
            project = dailyMap["project"].toDomainProject(),
            startTime = dailyMap["startTime"]?.let { time -> time as Long }
                ?: 0,
            state = dailyMap["state"] as String,
            queue = dailyMap["queue"]?.let { queue -> queue as List<String> }
                ?: emptyList(),
            users = dailyMap["users"].toUserList()
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun Any?.toDomainProject(): Project {
        val map = this as Map<String, Any>
        return Project(
            id = map["id"] as String,
            name = map["name"] as String
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun Any?.toUserList(): List<User> {
        val usersMap = this as Map<String, Map<String, Any>>
        return usersMap.map { (_, value) ->
            User(
                id = value["id"] as String,
                name = value["displayName"] as? String ?: "",
                email = value["email"] as String,
                active = value["active"] as Boolean
            )
        }
    }
}
