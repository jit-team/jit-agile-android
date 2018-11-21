package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.UserRepository

class LogoutCurrentUserUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<LogoutCurrentUserUseCase.Params, User>(dispatcher) {

    override suspend fun build(params: Params): Response<User> {
        return userRepository.logout()
    }

    object Params
}