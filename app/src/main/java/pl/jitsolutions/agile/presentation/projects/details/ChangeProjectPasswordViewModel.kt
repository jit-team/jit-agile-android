package pl.jitsolutions.agile.presentation.projects.details

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.usecases.ChangeProjectPasswordUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ChangeProjectPassword
import pl.jitsolutions.agile.utils.mutableLiveData

class ChangeProjectPasswordViewModel(
    private val changeProjectPasswordUseCase: ChangeProjectPasswordUseCase,
    private val navigator: Navigator,
    private val projectId: String,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val newPassword = mutableLiveData("")
    val state = mutableLiveData<State>(State.Idle)

    private val typedTextObserver = Observer<String> { state.value = State.Idle }

    init {
        newPassword.observeForever(typedTextObserver)
    }

    override fun onCleared() {
        newPassword.removeObserver(typedTextObserver)
        super.onCleared()
    }

    fun changePassword() = launch {
        state.value = State.InProgress
        val params = ChangeProjectPasswordUseCase.Params(projectId, newPassword.value!!)
        val result = changeProjectPasswordUseCase.executeAsync(params).await()

        when (result) {
            is Success -> {
                state.value = State.Success
                navigator.navigateBack(ChangeProjectPassword)
            }
            is Failure -> state.value = State.Fail(result.error)
        }
    }

    sealed class State {
        fun isErrorOfType(type: Error): Boolean {
            return this is State.Fail && this.type == type
        }

        object Success : State()
        object Idle : State()
        object InProgress : State()
        data class Fail(val type: Error) : State()
    }
}