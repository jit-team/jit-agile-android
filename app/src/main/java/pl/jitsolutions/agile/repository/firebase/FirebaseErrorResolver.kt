package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.functions.FirebaseFunctionsException
import pl.jitsolutions.agile.JitError
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.newErrorResponse
import java.net.UnknownHostException

object FirebaseErrorResolver {
    inline fun <reified T> parseFunctionException(exception: Exception): Response<T> {
        val error = when (exception) {
            is FirebaseFunctionsException -> {
                when (exception.code) {
                    FirebaseFunctionsException.Code.INVALID_ARGUMENT -> {
                        val message = exception.message
                        when {
                            message == null -> JitError.Unknown
                            message.contains("projectId") -> JitError.InvalidId
                            message.contains("projectName") -> JitError.InvalidName
                            message.contains("password") -> JitError.InvalidPassword
                            else -> JitError.Unknown
                        }
                    }
                    FirebaseFunctionsException.Code.ALREADY_EXISTS -> JitError.Exists
                    FirebaseFunctionsException.Code.NOT_FOUND -> JitError.DoesNotExist
                    else -> JitError.Unknown
                }
            }
            else -> parseCommonException(exception)
        }
        return newErrorResponse(error = error)
    }

    inline fun <reified T> parseResetPasswordException(exception: Exception): Response<T> {
        val error = when (exception) {
            is FirebaseAuthInvalidUserException -> JitError.DoesNotExist
            else -> parseCommonException(exception)
        }
        return newErrorResponse(error = error)
    }

    inline fun <reified T> parseLoginException(exception: Exception): Response<T> {
        val error = when (exception) {
            is FirebaseAuthInvalidUserException -> JitError.DoesNotExist
            is FirebaseAuthInvalidCredentialsException -> JitError.InvalidPassword
            else -> parseCommonException(exception)
        }
        return newErrorResponse(error = error)
    }

    inline fun <reified T> parseRegistrationException(exception: Exception): Response<T> {
        val error = when (exception) {
            is FirebaseAuthWeakPasswordException -> JitError.WeakPassword
            is FirebaseAuthInvalidCredentialsException -> JitError.InvalidEmail
            is FirebaseAuthUserCollisionException -> JitError.Exists
            else -> parseCommonException(exception)
        }
        return newErrorResponse(error = error)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun parseCommonException(exception: Exception): JitError {
        return when (exception) {
            is FirebaseNetworkException -> JitError.Network
            is UnknownHostException -> JitError.Network
            else -> JitError.Unknown
        }
    }
}