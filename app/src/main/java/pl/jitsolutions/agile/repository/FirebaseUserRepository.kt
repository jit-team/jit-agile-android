package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response

class FirebaseUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun login(email: String, password: String): ReceiveChannel<Response<User>> {
        return CoroutineScope(dispatcher).produce {
            try {
                val task = Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password))
                send(response(User(task.user.email!!)))
            } catch (e: Exception) {
                e.printStackTrace()
                send(errorResponse<User>(error = Response.ResponseError()))
            }
        }
    }
}