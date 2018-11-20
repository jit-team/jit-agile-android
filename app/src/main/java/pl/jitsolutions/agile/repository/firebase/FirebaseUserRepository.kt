package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.newErrorResponse
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
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { result ->
                            handleLoginResponse(result, continuation)
                        }
                } catch (e: Exception) {
                    continuation.resume(
                        newErrorResponse(error = FirebaseErrorResolver.parseLoginException(e))
                    )
                }
            }
        }.await()
    }

    private fun handleLoginResponse(
        result: Task<AuthResult>,
        continuation: Continuation<Response<Unit>>
    ) {
        when {
            result.isSuccessful -> {
                continuation.resume(response(Unit))
            }
            result.exception != null -> {
                continuation.resume(
                    newErrorResponse(
                        error = FirebaseErrorResolver.parseLoginException(result.exception!!)
                    )
                )
            }
        }
    }

    override suspend fun logout() = CoroutineScope(dispatcher).async {
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
            try {
                val registerTaskResult = suspendCoroutine<Task<AuthResult>> { continuation ->
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            continuation.resume(it)
                        }
                }
                handleRegisterResponse(userName, registerTaskResult)
            } catch (e: Exception) {
                errorResponse<Unit>(error = UserRepository.Error.UnknownError)
            }
        }.await()
    }

    private suspend fun handleRegisterResponse(
        userName: String,
        taskResult: Task<AuthResult>
    ): Response<Unit> {
        return when {
            taskResult.isSuccessful -> {
                val firebaseUser = taskResult.result?.user!!
                updateUserName(firebaseUser, userName)
                response(Unit)
            }
            taskResult.exception != null -> newErrorResponse(
                error = FirebaseErrorResolver.parseRegistrationException(
                    taskResult.exception!!
                )
            )
            else -> errorResponse(error = UserRepository.Error.UnknownError)
        }
    }

    override suspend fun getLoggedInUser(): Response<User?> {
        val loggedUser = firebaseAuth.currentUser?.toUser()
        return response(loggedUser)
    }

    private suspend fun updateUserName(firebaseUser: FirebaseUser, userName: String) {
        suspendCoroutine<User> { continuation ->
            firebaseUser.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            ).addOnCompleteListener {
                // todo: handling exception?
                continuation.resume(firebaseUser.toUser())
            }
        }
    }

    override suspend fun resetPassword(email: String): Response<Void?> {
        return CoroutineScope(dispatcher).async {
            try {
                suspendCoroutine<Response<Void?>> { continuation ->
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                        when {
                            it.isSuccessful -> continuation.resume(response(null))
                            it.exception != null -> continuation.resume(
                                errorResponse(error = retrieveError(it.exception!!))
                            )
                            else -> continuation.resume(errorResponse(error = UserRepository.Error.UnknownError))
                        }
                    }
                }
            } catch (e: Exception) {
                // TODO: add timber, for debug error in logs, for release send to crashlytics
                errorResponse<Void?>(error = UserRepository.Error.UnknownError)
            }
        }.await()
    }

    private fun retrieveError(exception: Exception): UserRepository.Error {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> UserRepository.Error.WeakPassword
            is FirebaseAuthInvalidUserException -> UserRepository.Error.UserNotFound
            is FirebaseAuthInvalidCredentialsException -> UserRepository.Error.InvalidPassword
            is FirebaseAuthUserCollisionException -> UserRepository.Error.UserAlreadyExist
            is FirebaseNetworkException -> UserRepository.Error.ServerConnection
            else -> UserRepository.Error.UnknownError
        }
    }
}