package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response

class FirebaseUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Response<User> {
        val loginResults = CoroutineScope(dispatcher).async {
            try {
                delay(2000)
                val task = Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password))
                response(User(task.user.email!!))
            } catch (e: Exception) {
                e.printStackTrace()
                errorResponse<User>(error = Unit)
            }
        }
        return loginResults.await()
    }

    override suspend fun register(email: String, password: String): Response<User> {
        val registerResults = CoroutineScope(dispatcher).async {
            try {
                val task = Tasks.await(firebaseAuth.createUserWithEmailAndPassword(email, password))
                response(User(task.user.email!!))
            } catch (e: Exception) {
                e.printStackTrace()
                errorResponse<User>(error = Unit)
            }
        }
        return registerResults.await()
    }
}