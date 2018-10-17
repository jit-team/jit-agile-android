package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.repository.UserRepository

class UserRegistrationSuccessFulUseCase(private val userRepository: UserRepository,
                                        private val dispatcher: CoroutineDispatcher)
    : UseCase<UserRegistrationSuccessFulUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        val user = response(userRepository.getLoggedInUser().data?.name!!)
        return user
    }

    data class Params(val void: Void? = null)
}





