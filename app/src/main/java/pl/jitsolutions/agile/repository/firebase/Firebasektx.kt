package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

fun <T> Task<T>.isResponseOk(): Boolean {
    val result = result
    return when (result) {
        is DocumentSnapshot -> {
            val cachedResponse = result.metadata.isFromCache
            val hasContent = result.data != null
            isSuccessful && hasContent && !cachedResponse
        }
        is QuerySnapshot -> {
            val cachedResponse = result.metadata.isFromCache
            val hasContent = true
            isSuccessful && hasContent && !cachedResponse
        }
        else -> false
    }
}