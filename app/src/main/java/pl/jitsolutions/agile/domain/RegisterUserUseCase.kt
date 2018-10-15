package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import pl.jitsolutions.agile.repository.UserRepository

class RegisterUserUseCase(private val userRepository: UserRepository,
                          dispatcher: CoroutineDispatcher)
    : UseCase<RegisterUserUseCase.Params, String>(dispatcher) {

    override suspend fun build(params: Params): Response<String> {
        return response(userRepository.register(params.email, params.password).data?.name ?: "")
    }

    data class Params(val email: String, val password: String)
}