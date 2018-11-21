package pl.jitsolutions.agile.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import pl.jitsolutions.agile.domain.Response
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.repository.UserRepository

class GetLoggedUserUseCase(
    private val userRepository: UserRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<GetLoggedUserUseCase.Params, User?>(dispatcher) {

    override suspend fun build(params: Params): Response<User?> {
        return userRepository.getLoggedInUser()
    }

    object Params
}