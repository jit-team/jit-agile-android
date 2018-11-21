package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import pl.jitsolutions.agile.repository.UserRepository
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseUserRepository(private val dispatcher: CoroutineDispatcher) :
    UserRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                try {
                    firebaseAuth
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { continuation.handleLoginResponse(it) }
                } catch (e: Exception) {
                    continuation.resume(FirebaseErrorResolver.parseLoginException(e))
                }
            }
        }.await()
    }

    private fun Continuation<Response<Unit>>.handleLoginResponse(result: Task<AuthResult>) {
        val response = when {
            result.isSuccessful -> response(Unit)
            result.exception != null -> FirebaseErrorResolver.parseLoginException(result.exception!!)
            else -> errorResponse(error = Error.Unknown)
        }
        resume(response)
    }

    override suspend fun logout() =
        CoroutineScope(dispatcher).async {
            val currentUser = firebaseAuth.currentUser
            firebaseAuth.signOut()
            response(currentUser!!.toUser())
        }.await()

    override suspend fun register(
        userName: String,
        email: String,
        password: String
    ): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                try {
                    firebaseAuth
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { continuation.handleRegisterResponse(it, userName) }
                } catch (e: Exception) {
                    continuation.resume(FirebaseErrorResolver.parseRegistrationException(e))
                }
            }
        }.await()
    }

    private fun Continuation<Response<Unit>>.handleRegisterResponse(
        result: Task<AuthResult>,
        userName: String
    ) {
        val response = when {
            result.isSuccessful -> {
                val firebaseUser = result.result?.user!!
                updateUserName(firebaseUser, userName)
                response(Unit)
            }
            result.exception != null -> FirebaseErrorResolver.parseRegistrationException(result.exception!!)
            else -> errorResponse(error = Error.Unknown)
        }
        resume(response)
    }

    private fun updateUserName(firebaseUser: FirebaseUser, userName: String) {
        val updateProfileTask = firebaseUser.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()
        )
        Tasks.await(updateProfileTask)
    }

    override suspend fun getLoggedInUser(): Response<User?> {
        val loggedUser = firebaseAuth.currentUser?.toUser()
        return response(loggedUser)
    }

    override suspend fun resetPassword(email: String): Response<Unit> {
        return CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<Unit>> { continuation ->
                try {
                    firebaseAuth
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener { continuation.handleResetPasswordResponse(it) }
                } catch (e: Exception) {
                    continuation.resume(FirebaseErrorResolver.parseResetPasswordException(e))
                }
            }
        }.await()
    }

    private fun Continuation<Response<Unit>>.handleResetPasswordResponse(result: Task<Void>) {
        val response = when {
            result.isSuccessful -> response(Unit)
            result.exception != null -> FirebaseErrorResolver.parseResetPasswordException(result.exception!!)
            else -> errorResponse(error = Error.Unknown)
        }
        resume(response)
    }
}