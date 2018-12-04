package pl.jitsolutions.agile.presentation.projects.details

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.jitsolutions.agile.common.Error
import pl.jitsolutions.agile.domain.Failure
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Success
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.DeleteProjectUseCase
import pl.jitsolutions.agile.domain.usecases.GetProjectUseCase
import pl.jitsolutions.agile.domain.usecases.JoinDailyUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveProjectUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ChangeProjectPassword
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Daily
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectDetails
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectDetailsViewModel(
    private val getProjectUseCase: GetProjectUseCase,
    private val leaveProjectUseCase: LeaveProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val joinDailyUseCase: JoinDailyUseCase,
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
        when (result) {
            is Success -> navigator.navigateBack(ProjectDetails(projectId))
            is Failure -> state.value = State.Fail(result.error)
        }
    }

    fun leaveProject() = launch {
        state.value = State.InProgress
        val params = LeaveProjectUseCase.Params(projectId)
        val result = leaveProjectUseCase.executeAsync(params).await()
        when (result) {
            is Success -> navigator.navigateBack(ProjectDetails(projectId))
            is Failure -> state.value = State.Fail(result.error)
        }
    }

    fun joinDaily() = launch {
        state.value = State.InProgress
        val params = JoinDailyUseCase.Params(projectId)
        val result = joinDailyUseCase.executeAsync(params).await()

        when (result) {
            is Success -> {
                navigator.navigate(
                    from = ProjectDetails(projectId),
                    to = Daily(projectId)
                )
                state.value = State.Success
            }
            is Failure -> state.value = State.Fail(result.error)
        }
    }

    fun changePassword() {
        navigator.navigate(ProjectDetails(projectId), ChangeProjectPassword)
    }

    private fun executeGetProjectDetails() = launch {
        state.value = State.InProgress
        val resultState = executeGetProject()
        state.value = resultState

        if (resultState is State.Fail) {
            navigator.navigateBack(ProjectDetails(projectId))
        }
    }

    private suspend fun executeGetProject(): State {
        val params = GetProjectUseCase.Params(projectId)
        val result = getProjectUseCase.executeAsync(params).await()
        return when (result) {
            is Success -> {
                val projectWithUsers = result.data
                project.value = projectWithUsers.project
                users.value = projectWithUsers.users
                State.Success
            }
            is Failure -> State.Fail(result.error)
        }
    }

    sealed class State {
        fun isErrorOfType(type: Error): Boolean {
            return this is State.Fail && this.type == type
        }

        object Success : State()
        object Idle : State()
        object InProgress : State()
        object Empty : State()
        data class Fail(val type: Error) : State()
    }
}