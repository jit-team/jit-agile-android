package pl.jitsolutions.agile.presentation.projects.details

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.DeleteProjectUseCase
import pl.jitsolutions.agile.domain.usecases.GetProjectUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveProjectUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectDetailsViewModel(
    private val getProjectUseCase: GetProjectUseCase,
    private val leaveProjectUseCase: LeaveProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val navigator: Navigator,
    private val projectId: String,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val project = MutableLiveData<Project>()
    val users = MutableLiveData<List<User>>()
    val state = mutableLiveData<State>(State.Idle)

    init {
        executeGetProjectDetails()
    }

    fun deleteProject() = launch {
        state.value = State.InProgress
        val params = DeleteProjectUseCase.Params(projectId)
        val result = deleteProjectUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigateBack(Navigator.Destination.ProjectDetails(projectId))
            ERROR -> handleDeleteProjectError(result)
        }
    }

    private fun handleDeleteProjectError(result: Response<Unit>) {
        val errorState = when (result.error) {
            DeleteProjectUseCase.Error.ServerConnection ->
                State.Error(ErrorType.CONNECTION)
            is DeleteProjectUseCase.Error.ProjectNotFound ->
                State.Error(ErrorType.PROJECT_NOT_FOUND)
            else -> State.Error(ErrorType.CONNECTION)
        }
        state.value = errorState
    }

    fun leaveProject() = launch {
        state.value = State.InProgress
        val params = LeaveProjectUseCase.Params(projectId)
        val result = leaveProjectUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigateBack(Navigator.Destination.ProjectDetails(projectId))
            ERROR -> handleLeaveProjectError(result)
        }
    }

    private fun handleLeaveProjectError(result: Response<Unit>) {
        val errorState = when (result.error) {
            LeaveProjectUseCase.Error.ServerConnection ->
                State.Error(ErrorType.CONNECTION)
            is LeaveProjectUseCase.Error.ProjectNotFound ->
                State.Error(ErrorType.PROJECT_NOT_FOUND)
            else -> State.Error(ErrorType.CONNECTION)
        }
        state.value = errorState
    }

    private fun executeGetProjectDetails() = launch {
        state.value = State.InProgress
        val resultState = executeGetProject()
        state.value = resultState

        if (resultState is State.Error) {
            navigator.navigateBack(Navigator.Destination.ProjectDetails(projectId))
        }
    }

    private suspend fun executeGetProject(): State {
        val params = GetProjectUseCase.Params(projectId)
        val result = getProjectUseCase.executeAsync(params).await()
        return when (result.status) {
            SUCCESS -> {
                with(result.data!!) {
                    project.value = first
                    users.value = second
                }
                State.Idle
            }
            ERROR -> when (result.error!!) {
                GetProjectUseCase.Error.ServerConnection ->
                    State.Error(ErrorType.CONNECTION)
                is GetProjectUseCase.Error.ProjectNotFound ->
                    State.Error(ErrorType.PROJECT_NOT_FOUND)
                else -> State.Error(ErrorType.CONNECTION)
            }
        }
    }

    sealed class State {
        fun isErrorOfType(type: ErrorType): Boolean {
            return this is State.Error && this.type == type
        }

        object Idle : State()
        object InProgress : State()
        object Empty : State()
        class Error(val type: ErrorType) : State()
    }

    enum class ErrorType { CONNECTION, PROJECT_NOT_FOUND }
}