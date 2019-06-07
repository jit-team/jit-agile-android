package pl.jitsolutions.agile.presentation.projects.managing

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.usecases.ProjectJoiningUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectDetails
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectJoining
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectJoiningViewModel(
    private val projectJoiningUseCase: ProjectJoiningUseCase,
    private val navigator: Navigator,
    dispatcher: CoroutineDispatcher
) : CoroutineViewModel(dispatcher) {
    val projectName = mutableLiveData("")
    val password = mutableLiveData("")
    val state = mutableLiveData<State>(State.None)

    private val typedTextObserver =
        Observer<String> { state.value = State.None }

    init {
        projectName.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun joinProject() = launch {
        state.value = State.InProgress
        val params = ProjectJoiningUseCase.Params(projectName.value!!, password.value!!)
        val result = projectJoiningUseCase.executeAsync(params).await()
        when (result) {
            is Success -> {
                state.value = State.Success
                val projectId = result.data
                navigator.navigate(ProjectJoining, ProjectDetails(projectId, false))
            }
            is Failure -> {
                state.value = State.Fail(result.error)
            }
        }
    }

    override fun onCleared() {
        projectName.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class State {
        fun isErrorOfType(type: Error): Boolean {
            return this is Fail && this.type == type
        }

        object None : State()
        object InProgress : State()
        data class Fail(val type: Error) : State()
        object Success : State()
    }
}