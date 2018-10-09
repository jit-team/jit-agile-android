package pl.jitsolutions.agile.domain

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository) {
    fun execute(email: String, password: String): MutableLiveData<User> {
        val userLiveData = MutableLiveData<User>()

        GlobalScope.launch {
            val user: User = withContext(Dispatchers.IO) {
                Thread.sleep(1000)
                userRepository.login(email, password)
            }
            withContext(Dispatchers.Main) {
                userLiveData.value = user
            }
        }
        return userLiveData
    }
}