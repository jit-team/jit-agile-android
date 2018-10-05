package pl.jitsolutions.agile.domain

import pl.jitsolutions.agile.repository.UserRepository

class GetCurrentUserUseCase(private val userRepository: UserRepository) {
    fun execute() : User {
        return userRepository.getCurrentUser()
    }
}