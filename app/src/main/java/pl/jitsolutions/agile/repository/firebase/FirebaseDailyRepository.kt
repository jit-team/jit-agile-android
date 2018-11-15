package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Daily
import pl.jitsolutions.agile.domain.Response
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
}
