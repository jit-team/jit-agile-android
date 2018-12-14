package pl.jitsolutions.agile.repository.firebase

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.UserRepository

class FirebaseUserRepository(
    private val firebaseAuth: FirebaseAuth,
    dispatcher: CoroutineDispatcher
) : UserRepository {
    private val scope = CoroutineScope(dispatcher)

    override suspend fun login(email: String, password: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = firebaseAuth.signInWithEmailAndPassword(email, password)
                    Tasks.await(task)

                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseLoginException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseLoginException<Unit>(e)
                }
            }.await()
        }
    }

    override suspend fun logout() =
        retryWhenError {
            scope.async {
                val currentUser = firebaseAuth.currentUser
                firebaseAuth.signOut()
                Success(currentUser!!.toUser())
            }.await()
        }

    override suspend fun register(
        userName: String,
        email: String,
        password: String
    ): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
                    val result = Tasks.await(task)
                    if (task.isSuccessful) {
                        val firebaseUser = result.user!!
                        updateUserName(firebaseUser, userName)
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseRegistrationException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseRegistrationException<Unit>(e)
                }
            }.await()
        }
    }

    private fun updateUserName(firebaseUser: FirebaseUser, userName: String) {
        val updateProfileTask = firebaseUser.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()
        )
        // TODO error handling
        Tasks.await(updateProfileTask)
    }

    override suspend fun getLoggedInUser(): Response<User> {
        val loggedUser = firebaseAuth.currentUser?.toUser()
        return if (loggedUser == null) {
            Failure(Error.DoesNotExist)
        } else {
            Success(loggedUser)
        }
    }

    override suspend fun resetPassword(email: String): Response<Unit> {
        return retryWhenError {
            scope.async {
                try {
                    val task = firebaseAuth.sendPasswordResetEmail(email)
                    Tasks.await(task)
                    if (task.isSuccessful) {
                        Success(Unit)
                    } else {
                        FirebaseErrorResolver.parseResetPasswordException<Unit>(
                            task.exception ?: Exception()
                        )
                    }
                } catch (e: Exception) {
                    FirebaseErrorResolver.parseResetPasswordException<Unit>(e)
                }
            }.await()
        }
    }
}