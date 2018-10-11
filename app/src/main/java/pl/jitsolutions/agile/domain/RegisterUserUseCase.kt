package pl.jitsolutions.agile.domain

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.channels.ProducerScope
import pl.jitsolutions.agile.repository.UserRepository

class RegisterUserUseCase(private val userRepository: UserRepository,
                          dispatcher: CoroutineDispatcher)
    : UseCase<RegisterUserUseCase.Params, String>(dispatcher) {

    override suspend fun ProducerScope<Response<String>>.buildLogic(params: Params) {
        userRepository.register(params.email, params.password)
    }

    data class Params(val email: String, val password: String)
}