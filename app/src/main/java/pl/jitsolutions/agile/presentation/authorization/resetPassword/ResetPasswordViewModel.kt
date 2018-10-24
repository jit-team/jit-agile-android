package pl.jitsolutions.agile.presentation.authorization.resetPassword

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.UserResetPasswordUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData

class ResetPasswordViewModel(private val resetPasswordUseCase: UserResetPasswordUseCase,
                             val navigator: Navigator,
                             dispatcher: CoroutineDispatcher) : CoroutineViewModel(dispatcher) {

    val email = mutableLiveData("")
    val resetPasswordState = mutableLiveData<ResetPasswordState>(ResetPasswordState.None)

    private val typedTextObserver = Observer<String> {
        resetPasswordState.value = ResetPasswordState.None
    }

    init {
        email.observeForever(typedTextObserver)
    }


    fun resetPassword() = launch {
        resetPasswordState.value = ResetPasswordState.InProgress
        val params = UserResetPasswordUseCase.Params(email.value!!)
        val response = resetPasswordUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                resetPasswordState.value = ResetPasswordState.Success
                navigator.navigateBack(from = Navigator.Destination.RESET_PASSWORD)
            }
            Response.Status.ERROR -> {
                val type = when (response.error) {
                    is UserResetPasswordUseCase.Error.UnknownError -> ResetPasswordTypeError.SERVER
                    is UserResetPasswordUseCase.Error.ServerConnection -> ResetPasswordTypeError.SERVER
                    is UserResetPasswordUseCase.Error.UserEmailNotFound -> ResetPasswordTypeError.EMAIL_NOT_FOUND
                    is UserResetPasswordUseCase.Error.InvalidEmail -> ResetPasswordTypeError.EMAIL
                    is UserResetPasswordUseCase.Error.EmptyEmail -> ResetPasswordTypeError.EMAIL
                    else -> ResetPasswordViewModel.ResetPasswordTypeError.SERVER
                }
                resetPasswordState.value = ResetPasswordState.Error(type)
            }
        }
    }

    sealed class ResetPasswordState {
        fun isErrorOfType(type: ResetPasswordTypeError): Boolean {
            return this is Error && this.type == type
        }

        object None : ResetPasswordState()
        object InProgress : ResetPasswordState()
        data class Error(val type: ResetPasswordTypeError) : ResetPasswordState()
        object Success : ResetPasswordState()
    }

    enum class ResetPasswordTypeError { EMAIL, EMAIL_NOT_FOUND, SERVER, UNKNOWN}
}
