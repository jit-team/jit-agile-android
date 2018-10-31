package pl.jitsolutions.agile.presentation.projects

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import pl.jitsolutions.agile.domain.Project
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.usecases.*
import pl.jitsolutions.agile.presentation.common.CoroutineViewModel
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Login
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectDetails
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.utils.mutableLiveData

class ProjectListViewModel(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val logoutCurrentUserUseCase: LogoutCurrentUserUseCase,
    private val getApplicationVersionUseCase: GetApplicationVersionUseCase,
    private val getCurrentUserProjectsUseCase: GetCurrentUserProjectsUseCase,
    private val navigator: Navigator,
    mainDispatcher: CoroutineDispatcher
) : CoroutineViewModel(mainDispatcher) {
    val user = mutableLiveData<User?>(null)
    val version = mutableLiveData("")
    val projects = MutableLiveData<List<Project>>()
    val projectListState = mutableLiveData<ProjectListState>(ProjectListState.None)

    init {
        projectListState.value = ProjectListState.InProgress
        executeGetLoggedUser()
        executeGetApplicationVersion()
        executeGetUserProjects()
    }

    fun logout() = launch {
        val params = LogoutCurrentUserUseCase.Params()
        val result = logoutCurrentUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> navigator.navigate(ProjectList, Login)
            ERROR -> throw result.error!!
        }
    }

    fun showProjectDetails(projectId: String) {
        navigator.navigate(from = ProjectList, to = ProjectDetails(projectId))
    }

    private fun executeGetLoggedUser() = launch {
        val params = GetLoggedUserUseCase.Params()
        val result = getLoggedUserUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> handleGetLoggedUserSuccess(result)
            ERROR -> throw result.error!!
        }
    }

    private fun executeGetUserProjects() = launch {
        delay(1000)
        val params = GetCurrentUserProjectsUseCase.Params()
        val result = getCurrentUserProjectsUseCase.executeAsync(params).await()
        when (result.status) {
            SUCCESS -> {
                if (result.data != null && !result.data.isEmpty()) {
                    projectListState.value = ProjectListState.Success
                    projects.value = result.data
                } else {
                    projectListState.value = ProjectListState.EmptyList
                }
            }
            ERROR -> {
                val type = when (result.error) {
                    GetCurrentUserProjectsUseCase.Error.ServerConnection -> ProjectListError.SERVER
                    GetCurrentUserProjectsUseCase.Error.UserNotFound -> ProjectListError.USER_NOT_FOUND
                    else -> ProjectListError.UNKNOWN

                }
                projectListState.value = ProjectListState.Error(type)
            }
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
            ERROR -> throw result.error!!
        }
    }

    sealed class ProjectListState {
        object None : ProjectListState()
        object InProgress : ProjectListState()
        object EmptyList : ProjectListState()
        data class Error(val type: ProjectListError) : ProjectListState()
        object Success : ProjectListState()
    }

    enum class ProjectListError { UNKNOWN, SERVER, USER_NOT_FOUND }
}