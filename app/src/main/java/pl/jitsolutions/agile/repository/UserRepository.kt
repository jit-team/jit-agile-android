package pl.jitsolutions.agile.repository

import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User

interface UserRepository {
    suspend fun login(email: String, password: String): Response<User>
    suspend fun logout(): Response<User>
    suspend fun register(userName: String, email: String, password: String): Response<User>
    suspend fun getLoggedInUser(): Response<User?>
    suspend fun resetPassword(email: String): Response<Void?>

    sealed class Error(message: String? = null) : Throwable(message) {
        object InvalidEmail : Error()
        object InvalidPassword : Error()
        object UserNotFound : Error()
        object WeakPassword : Error()
        object UserAlreadyExist : Error()
        object UnknownError : Error()
        object ServerConnection : Error()
    }
}