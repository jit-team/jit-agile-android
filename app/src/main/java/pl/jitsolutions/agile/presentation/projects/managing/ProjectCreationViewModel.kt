package pl.jitsolutions.agile.presentation.projects.managing

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.ProjectCreationUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectCreation
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectCreationViewModel(
    private val projectCreationUseCase: ProjectCreationUseCase,
    private val navigator: Navigator,
    dispatcher: CoroutineDispatcher
) : CoroutineViewModel(dispatcher) {
    val projectName = mutableLiveData("")
    val password = mutableLiveData("")
    val projectCreationState = mutableLiveData<ProjectCreationState>(ProjectCreationState.None)

    private val typedTextObserver =
        Observer<String> { projectCreationState.value = ProjectCreationState.None }

    init {
        projectName.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun createProject() = launch {
        projectCreationState.value = ProjectCreationState.InProgress
        val result =
            projectCreationUseCase.executeAsync(
                ProjectCreationUseCase.Params(
                    projectName.value!!,
                    password.value!!
                )
            )
                .await()
        when (result.status) {
            Response.Status.SUCCESS -> {
                projectCreationState.value = ProjectCreationState.Success
            }
            Response.Status.ERROR -> {
                val error = when (result.error) {
                    is ProjectCreationUseCase.Error.EmptyProjectName -> ProjectCreationErrorType.PROJECT_NAME
                    is ProjectCreationUseCase.Error.EmptyPassword -> ProjectCreationErrorType.PASSWORD
                    is ProjectCreationUseCase.Error.ProjectAlreadyExist -> ProjectCreationErrorType.PROJECT_ALREADY_EXIST
                    else -> ProjectCreationErrorType.UNKNOWN
                }
                projectCreationState.value = ProjectCreationState.Error(error)
            }
        }
    }

    fun confirmSuccess() {
        navigator.navigateBack(ProjectCreation)
    }

    override fun onCleared() {
        projectName.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class ProjectCreationState {
        fun isErrorOfType(type: ProjectCreationErrorType): Boolean {
            return this is Error && this.type == type
        }

        object None : ProjectCreationState()
        object InProgress : ProjectCreationState()
        data class Error(val type: ProjectCreationErrorType) : ProjectCreationState()
        object Success : ProjectCreationState()
    }

    enum class ProjectCreationErrorType { PROJECT_NAME, PROJECT_ALREADY_EXIST, PASSWORD, SERVER, UNKNOWN }
}