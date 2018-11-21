package pl.jitsolutions.agile.presentation.projects

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.Error
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.FAILURE
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.GetApplicationVersionUseCase
import pl.jitsolutions.agile.domain.usecases.GetCurrentUserProjectsWithDailyUseCase
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.usecases.JoinDailyUseCase
import pl.jitsolutions.agile.domain.usecases.LogoutCurrentUserUseCase
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Login
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectAdding
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectDetails
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectListViewModel(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val logoutCurrentUserUseCase: LogoutCurrentUserUseCase,
    private val getApplicationVersionUseCase: GetApplicationVersionUseCase,
    private val getCurrentUserProjectsWithDailyUseCase: GetCurrentUserProjectsWithDailyUseCase,
    private val joinDailyUseCase: JoinDailyUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val user = mutableLiveData<User?>(null)
    val version = mutableLiveData("")
    val projects = MutableLiveData<List<ProjectWithDaily>>()
    val state = mutableLiveData<State>(State.None)

    private val navigationObserver = object : Navigator.NavigationObserver {
        override fun onNavigation(destination: Navigator.Destination) = refresh()
    }

    init {
        state.value = State.InProgress
        executeGetLoggedUser()
        executeGetApplicationVersion()
        executeGetUserProjects(moveToDaily = true)

        navigator.addDestinationObserver(ProjectList, navigationObserver)
    }

    override fun onCleared() {
        super.onCleared()
        navigator.removeDestinationObserver(ProjectList, navigationObserver)
    }

    fun logout() = launch {
        val params = LogoutCurrentUserUseCase.Params
        val result = logoutCurrentUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigate(from = ProjectList, to = Login)
            FAILURE -> state.value = State.Fail(result.error!!)
        }
    }

    fun refresh() {
        executeGetUserProjects()
    }

    fun joinDaily(projectId: String) = launch {
        val params = JoinDailyUseCase.Params(projectId)
        val result = joinDailyUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigate(ProjectList, Navigator.Destination.Daily(projectId))
            FAILURE -> state.value = State.Fail(result.error!!)
        }
    }

    fun createProject() {
        navigator.navigate(from = ProjectList, to = ProjectAdding)
    }

    fun showProjectDetails(projectId: String) {
        navigator.navigate(from = ProjectList, to = ProjectDetails(projectId))
    }

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params
        val result = getLoggedUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> handleGetLoggedUserSuccess(result)
            FAILURE -> state.value = State.Fail(result.error!!)
        }
    }

    private fun executeGetUserProjects(moveToDaily: Boolean = false) = launch {
        val params = GetCurrentUserProjectsWithDailyUseCase.Params
        val result = getCurrentUserProjectsWithDailyUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> {
                if (result.data != null && !result.data.isEmpty()) {
                    state.value = State.Success
                    val projectsWithDaily = result.data
                    projects.value = projectsWithDaily.sortedBy { it.project.name }
                    moveToDailyIfActive(moveToDaily, projectsWithDaily)
                } else {
                    projects.value = emptyList()
                    state.value = State.Empty
                }
            }
            FAILURE -> {
                state.value = State.Fail(result.error!!)
            }
        }
    }

    private fun moveToDailyIfActive(
        moveToDaily: Boolean,
        projectsWithDaily: List<ProjectWithDaily>
    ) {
        if (moveToDaily && projectsWithDaily.any { it.daily != null }) {
            val dailyId = projectsWithDaily.first { it.daily != null }.project.id
            joinDaily(dailyId)
        }
    }

    private fun handleGetLoggedUserSuccess(response: Response<User?>) {
        if (response.data != null) {
            user.value = response.data
        } else {
            navigator.navigate(from = ProjectList, to = Login)
        }
    }

    private fun executeGetApplicationVersion() = launch {
        val params = GetApplicationVersionUseCase.Params
        val result = getApplicationVersionUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> version.value = result.data!!
            FAILURE -> state.value = State.Fail(result.error!!)
        }
    }

    sealed class State {
        fun isErrorOfType(type: Error): Boolean {
            return this is ProjectListViewModel.State.Fail && this.type == type
        }

        object None : State()
        object InProgress : State()
        object Empty : State()
        data class Fail(val type: Error) : State()
        object Success : State()
    }
}