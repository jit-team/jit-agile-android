package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import pl.jitsolutions.agile.domain.*

class FirebaseUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun login(email: String, password: String): ReceiveChannel<Response<User>> {
        return CoroutineScope(dispatcher).produce {
            send(inProgressResponse())
            try {
                delay(2000)
                val task = Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password))
                send(response(User(task.user.email!!)))
            } catch (e: Exception) {
                e.printStackTrace()
                send(errorResponse(error = Unit))
            }
        }
    }

    override fun register(email: String, password: String): ReceiveChannel<Response<User>> {
        return CoroutineScope(dispatcher).produce {
            try {
                val task = Tasks.await(firebaseAuth.createUserWithEmailAndPassword(email, password))
                send(response(User(task.user.email!!)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}