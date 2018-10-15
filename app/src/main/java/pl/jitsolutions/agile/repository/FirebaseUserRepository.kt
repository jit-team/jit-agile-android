package pl.jitsolutions.agile.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.domain.response
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseUserRepository(private val dispatcher: CoroutineDispatcher) : UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Response<User> {
        val loginResults = CoroutineScope(dispatcher).async {
            suspendCoroutine<Response<User>> { continuation ->
                try {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
                        when {
                            result.isSuccessful -> continuation.resume(response(User(result.result?.user?.email!!)))
                            result.exception != null -> {
                                continuation.resume(errorResponse(error = retrieveErrorText(result.exception!!)))
                            }
                        }
                    }
                } catch (e: Exception) {
                    continuation.resume(errorResponse(error = UserRepository.Error.UnknownError))
                }
            }
        }
        return loginResults.await()
    }

    override suspend fun register(userName: String, email: String, password: String): Response<User> {
        return CoroutineScope(dispatcher).async {
            try {
                suspendCoroutine<Task<AuthResult>> { continuation ->
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        continuation.resume(it)
                    }
                }.let {
                    when {
                        it.isSuccessful -> {
                            updateUserName(userName, it.result?.user!!)
                            response(User(userName))
                        }
                        it.exception != null -> errorResponse(error = retrieveErrorText(it.exception!!))
                        else -> errorResponse(error = UserRepository.Error.UnknownError)
                    }
                }
            } catch (e: Exception) {
                errorResponse<User>(error = UserRepository.Error.UnknownError)
            }
        }.await()
    }

    private suspend fun updateUserName(userName: String, firebaseUser: FirebaseUser) {
        suspendCoroutine<String> { continuation ->
            firebaseUser.updateProfile(UserProfileChangeRequest.Builder()
                    .setDisplayName(userName).build()).addOnCompleteListener {
                //todo: handling exception?
                continuation.resume(userName)
            }
        }
    }

    private fun retrieveErrorText(exception: Exception): UserRepository.Error {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> UserRepository.Error.WeakPassword
            is FirebaseAuthInvalidCredentialsException -> UserRepository.Error.InvalidCredentials
            is FirebaseAuthInvalidUserException -> UserRepository.Error.InvalidCredentials
            is FirebaseAuthUserCollisionException -> UserRepository.Error.UserAlreadyExist
            else -> UserRepository.Error.UnknownError
        }
    }
}