package pl.jitsolutions.agile.presentation.projects

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.ProjectWithDaily
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.GetApplicationVersionUseCase
import pl.jitsolutions.agile.domain.usecases.GetCurrentUserProjectsUseCase
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

    init {
        state.value = State.InProgress
        executeGetLoggedUser()
        executeGetApplicationVersion()
        executeGetUserProjects(moveToDaily = true)
    }

    fun logout() = launch {
        val params = LogoutCurrentUserUseCase.Params()
        val result = logoutCurrentUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigate(ProjectList, Login)
            ERROR -> state.value = State.Error(ProjectListError.UNKNOWN)
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
            ERROR -> state.value = State.Error(ProjectListError.UNKNOWN)
        }
    }

    fun createProject() {
        navigator.navigate(from = ProjectList, to = ProjectAdding)
    }

    fun showProjectDetails(projectId: String) {
        navigator.navigate(from = ProjectList, to = ProjectDetails(projectId))
    }

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params()
        val result = getLoggedUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> handleGetLoggedUserSuccess(result)
            ERROR -> state.value = State.Error(ProjectListError.UNKNOWN)
        }
    }

    private fun executeGetUserProjects(moveToDaily: Boolean = false) = launch {
        val params = GetCurrentUserProjectsWithDailyUseCase.Params()
        val result = getCurrentUserProjectsWithDailyUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> {
                if (result.data != null && !result.data.isEmpty()) {
                    state.value = State.Success
                    val projectsWithDaily = result.data
                    projects.value = projectsWithDaily
                    moveToDailyIfActive(moveToDaily, projectsWithDaily)
                } else {
                    state.value = State.EmptyList
                }
            }
            ERROR -> {
                val type = when (result.error) {
                    GetCurrentUserProjectsUseCase.Error.ServerConnection -> ProjectListError.SERVER
                    GetCurrentUserProjectsUseCase.Error.UserNotFound -> ProjectListError.USER_NOT_FOUND
                    else -> ProjectListError.UNKNOWN
                }
                state.value = State.Error(type)
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
        val params = GetApplicationVersionUseCase.Params()
        val result = getApplicationVersionUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> version.value = result.data!!
            ERROR -> state.value = State.Error(ProjectListError.UNKNOWN)
        }
    }

    sealed class State {
        fun isErrorOfType(type: ProjectListViewModel.ProjectListError): Boolean {
            return this is ProjectListViewModel.State.Error && this.type == type
        }

        object None : State()
        object InProgress : State()
        object EmptyList : State()
        data class Error(val type: ProjectListError) : State()
        object Success : State()
    }

    enum class ProjectListError { UNKNOWN, SERVER, USER_NOT_FOUND }
}