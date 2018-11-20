package pl.jitsolutions.agile.presentation.authorization.resetPassword

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.UserResetPasswordUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData

class ResetPasswordViewModel(
    private val resetPasswordUseCase: UserResetPasswordUseCase,
    val navigator: Navigator,
    dispatcher: CoroutineDispatcher
) : CoroutineViewModel(dispatcher) {

    val email = mutableLiveData("")
    val state = mutableLiveData<State>(State.None)

    private val typedTextObserver = Observer<String> {
        state.value = State.None
    }

    init {
        email.observeForever(typedTextObserver)
    }

    fun resetPassword() = launch {
        state.value = State.InProgress
        val params = UserResetPasswordUseCase.Params(email.value!!)
        val response = resetPasswordUseCase.executeAsync(params).await()
        when (response.status) {
            Response.Status.SUCCESS -> {
                state.value = State.Success
                navigator.navigateBack(from = Navigator.Destination.ResetPassword)
            }
            Response.Status.FAILURE -> {
                val type = when (response.error) {
                    is UserResetPasswordUseCase.Error.UnknownError -> ResetPasswordTypeError.SERVER
                    is UserResetPasswordUseCase.Error.ServerConnection -> ResetPasswordTypeError.SERVER
                    is UserResetPasswordUseCase.Error.UserEmailNotFound -> ResetPasswordTypeError.EMAIL_NOT_FOUND
                    is UserResetPasswordUseCase.Error.InvalidEmail -> ResetPasswordTypeError.EMAIL
                    is UserResetPasswordUseCase.Error.EmptyEmail -> ResetPasswordTypeError.EMAIL
                    else -> ResetPasswordViewModel.ResetPasswordTypeError.SERVER
                }
                state.value = State.Fail(type)
            }
        }
    }

    fun confirmSuccess() {
        navigator.navigateBack(Navigator.Destination.ResetPassword)
    }

    sealed class State {
        fun isErrorOfType(type: ResetPasswordTypeError): Boolean {
            return this is Fail && this.type == type
        }

        object None : State()
        object InProgress : State()
        data class Fail(val type: ResetPasswordTypeError) : State()
        object Success : State()
    }

    enum class ResetPasswordTypeError { EMAIL, EMAIL_NOT_FOUND, SERVER, UNKNOWN }
}
