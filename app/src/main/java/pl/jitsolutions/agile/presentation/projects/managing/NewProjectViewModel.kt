package pl.jitsolutions.agile.presentation.projects.managing

import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.usecases.CreateNewProjectUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.utils.mutableLiveData

class NewProjectViewModel(
    private val createNewProjectUseCase: CreateNewProjectUseCase,
    dispatcher: CoroutineDispatcher
) : CoroutineViewModel(dispatcher) {
    val projectName = mutableLiveData("")
    val password = mutableLiveData("")
    val newProjectState = mutableLiveData<NewProjectState>(NewProjectState.None)

    private val typedTextObserver =
        Observer<String> { newProjectState.value = NewProjectState.None }

    init {
        projectName.observeForever(typedTextObserver)
        password.observeForever(typedTextObserver)
    }

    fun createNewProject() {
        launch {
            newProjectState.value = NewProjectState.InProgress
            val result =
                createNewProjectUseCase.executeAsync(
                    CreateNewProjectUseCase.Params(
                        projectName.value!!,
                        password.value!!
                    )
                )
                    .await()
            when (result.status) {
                Response.Status.SUCCESS -> {
                }
                Response.Status.ERROR -> {
                    val error = when (result.error) {
                        is CreateNewProjectUseCase.Error.EmptyProjectName -> NewProjectErrorType.PROJECT_NAME
                        is CreateNewProjectUseCase.Error.EmptyPassword -> NewProjectErrorType.PASSWORD
                        is CreateNewProjectUseCase.Error.ProjectAlreadyExist -> NewProjectErrorType.PROJECT_ALREADY_EXIST
                        else -> NewProjectErrorType.UNKNOWN
                    }
                    newProjectState.value = NewProjectState.Error(error)
                }
            }
        }
    }

    override fun onCleared() {
        projectName.removeObserver(typedTextObserver)
        password.removeObserver(typedTextObserver)
        super.onCleared()
    }

    sealed class NewProjectState {
        fun isErrorOfType(type: NewProjectErrorType): Boolean {
            return this is Error && this.type == type
        }

        object None : NewProjectState()
        object InProgress : NewProjectState()
        data class Error(val type: NewProjectErrorType) : NewProjectState()
        object Success : NewProjectState()
    }

    enum class NewProjectErrorType { PROJECT_NAME, PROJECT_ALREADY_EXIST, PASSWORD, SERVER, UNKNOWN }
}