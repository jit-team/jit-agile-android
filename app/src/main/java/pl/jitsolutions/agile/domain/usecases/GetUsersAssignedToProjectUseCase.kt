package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.Response.Status.ERROR
import pl.jitsolutions.agile.domain.Response.Status.SUCCESS
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.domain.errorResponse
import pl.jitsolutions.agile.repository.UserRepository

class GetUsersAssignedToProjectUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetUsersAssignedToProjectUseCase.Params, List<User>>(dispatcher) {

    override suspend fun build(params: Params): Response<List<User>> {
        val response = userRepository.getUsersAssignedToProject(params.projectId)
        return when (response.status) {
            SUCCESS -> response
            ERROR -> errorResponse(error = Error.ServerConnection)
        }
    }

    class Params(val projectId: String)

    sealed class Error(message: String? = null) : Throwable(message) {
        object ServerConnection : Error("Server connection error")
    }
}
