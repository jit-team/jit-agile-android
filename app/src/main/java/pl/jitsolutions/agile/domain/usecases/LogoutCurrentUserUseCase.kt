package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.UserRepository

class LogoutCurrentUserUseCase(private val userRepository: UserRepository,
                               dispatcher: CoroutineDispatcher
) : UseCase<LogoutCurrentUserUseCase.Params, User>(dispatcher) {

    override suspend fun build(params: Params): Response<User> {
        val response = userRepository.logout()
        return when (response.status) {
            Response.Status.SUCCESS -> response
            // Should not happen, we clear only the local Firebase cache for now
            Response.Status.ERROR -> throw response.error!!
        }
    }

    data class Params(val stub: String? = null)
}