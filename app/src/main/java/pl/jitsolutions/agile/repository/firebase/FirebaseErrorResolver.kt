package pl.jitsolutions.agile.repository.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.functions.FirebaseFunctionsException
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.errorResponse
import java.net.UnknownHostException

object FirebaseErrorResolver {
    inline fun <reified T> parseFunctionException(exception: Exception): Response<T> {
        val cause = exception.cause
        val error = when (cause) {
            is FirebaseFunctionsException -> {
                when (cause.code) {
                    FirebaseFunctionsException.Code.INVALID_ARGUMENT -> {
                        val message = exception.message
                        when {
                            message == null -> Error.Unknown
                            message.contains("projectId") -> Error.InvalidId
                            message.contains("projectName") -> Error.InvalidName
                            message.contains("password") -> Error.InvalidPassword
                            else -> Error.Unknown
                        }
                    }
                    FirebaseFunctionsException.Code.ALREADY_EXISTS -> Error.Exists
                    FirebaseFunctionsException.Code.NOT_FOUND -> Error.DoesNotExist
                    else -> Error.Unknown
                }
            }
            else -> parseCommonException(exception)
        }
        return errorResponse(error = error)
    }

    inline fun <reified T> parseResetPasswordException(exception: Exception): Response<T> {
        val error = when (exception.cause) {
            is FirebaseAuthInvalidUserException -> Error.DoesNotExist
            else -> parseCommonException(exception)
        }
        return errorResponse(error = error)
    }

    inline fun <reified T> parseLoginException(exception: Exception): Response<T> {
        val error = when (exception.cause) {
            is FirebaseAuthInvalidUserException -> Error.DoesNotExist
            is FirebaseAuthInvalidCredentialsException -> {
                val message = exception.message ?: ""
                when {
                    message.contains("email") -> Error.InvalidEmail
                    else -> Error.InvalidPassword
                }
            }
            else -> parseCommonException(exception)
        }
        return errorResponse(error = error)
    }

    inline fun <reified T> parseRegistrationException(exception: Exception): Response<T> {
        val error = when (exception.cause) {
            is FirebaseAuthWeakPasswordException -> Error.WeakPassword
            is FirebaseAuthInvalidCredentialsException -> {
                val message = exception.message ?: ""
                when {
                    message.contains("email") -> Error.InvalidEmail
                    else -> Error.InvalidPassword
                }
            }
            is FirebaseAuthUserCollisionException -> Error.Exists
            else -> parseCommonException(exception)
        }
        return errorResponse(error = error)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun parseCommonException(exception: Exception): Error {
        return when (exception.cause) {
            is FirebaseNetworkException -> Error.Network
            is UnknownHostException -> Error.Network
            else -> Error.Unknown
        }
    }
}