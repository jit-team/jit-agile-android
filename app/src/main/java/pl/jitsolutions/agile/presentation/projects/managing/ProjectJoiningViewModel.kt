package pl.jitsolutions.agile.presentation.projects.managing

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
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
    val projectJoiningState = mutableLiveData<ProjectJoiningState>(ProjectJoiningState.None)

    private val typedTextObserver =
        Observer<String> { projectJoiningState.value = ProjectJoiningState.None }

    init {
        projectName.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun joinProject() = launch {
        projectJoiningState.value = ProjectJoiningState.InProgress
        val result =
            projectJoiningUseCase.executeAsync(
                ProjectJoiningUseCase.Params(
                    projectName.value!!,
                    password.value!!
                )
            ).await()
        when (result.status) {
            Response.Status.SUCCESS -> {
                projectJoiningState.value = ProjectJoiningState.Success
                val projectId = result.data!!
                navigator.navigate(ProjectJoining, ProjectDetails(projectId))
            }
            Response.Status.FAILURE -> {
                val error = when (result.error) {
                    is ProjectJoiningUseCase.Error.EmptyProjectName -> ProjectJoiningErrorType.PROJECT_NAME
                    is ProjectJoiningUseCase.Error.EmptyPassword -> ProjectJoiningErrorType.PASSWORD
                    is ProjectJoiningUseCase.Error.ProjectNotFound -> ProjectJoiningErrorType.PROJECT_NOT_FOUND
                    is ProjectJoiningUseCase.Error.ServerConnection -> ProjectJoiningErrorType.SERVER
                    is ProjectJoiningUseCase.Error.InvalidPassword -> ProjectJoiningErrorType.INVALID_PASSWORD
                    else -> ProjectJoiningErrorType.UNKNOWN
                }
                projectJoiningState.value = ProjectJoiningState.Error(error)
            }
        }
    }

    override fun onCleared() {
        projectName.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class ProjectJoiningState {
        fun isErrorOfType(type: ProjectJoiningErrorType): Boolean {
            return this is Error && this.type == type
        }

        object None : ProjectJoiningState()
        object InProgress : ProjectJoiningState()
        data class Error(val type: ProjectJoiningErrorType) : ProjectJoiningState()
        object Success : ProjectJoiningState()
    }

    enum class ProjectJoiningErrorType { PROJECT_NAME, PROJECT_NOT_FOUND, PASSWORD, SERVER, UNKNOWN, INVALID_PASSWORD }
}