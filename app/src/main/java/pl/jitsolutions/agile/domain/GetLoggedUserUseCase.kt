package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.repository.UserRepository

class GetLoggedUserUseCase(private val userRepository: UserRepository,
                           private val dispatcher: CoroutineDispatcher)
    : UseCase<GetLoggedUserUseCase.Params, User?>(dispatcher) {

    override suspend fun build(params: Params): Response<User?> {
        return userRepository.getLoggedInUser()
    }

    data class Params(val void: Void? = null)
}





