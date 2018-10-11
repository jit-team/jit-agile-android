package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import pl.jitsolutions.agile.domain.User

class FirebaseUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun login(email: String, password: String): ReceiveChannel<User> {
        return CoroutineScope(dispatcher).produce {
            try {
                val task = Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password))
                send(User(task.user.email!!))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}