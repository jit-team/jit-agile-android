package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
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

    @Suppress("UNCHECKED_CAST")
    override suspend fun getDaily(dailyId: String): Response<Daily> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Daily>> { continuation ->
                firestore.collection("dailies")
                    .document(dailyId)
                    .get()
                    .addOnSuccessListener {
                        continuation.resume(response(it.data!!.toDomainDaily()))
                    }
                    .addOnFailureListener {
                        continuation.resume(errorResponse(error = it))
                    }
            }
        }.await()
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
