package pl.jitsolutions.agile.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.experimental.*
import pl.jitsolutions.agile.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository)
    : UseCase<LoginUserUseCase.Params, User>(Dispatchers.Default, Dispatchers.Main) {
    override fun run(params: Params): LiveData<User> {
        return userRepository.login(params.email, params.password)
    }

    data class Params(val email: String, val password: String)
}

abstract class UseCase<Params, Result>(private val executionDispatcher: CoroutineDispatcher,
                                       private val postActionDispatcher: CoroutineDispatcher) {

    private var resultLiveData: LiveData<Result>? = null
    private var observer: Observer<Result>? = null

    fun execute(params: Params): LiveData<Result> {
        val liveData = MutableLiveData<Result>()

        GlobalScope.launch(executionDispatcher) {
            resultLiveData = run(params)
            withContext(postActionDispatcher) {
                observer = Observer {
                    liveData.value = it
                }
                resultLiveData?.observeForever(observer!!)
            }
        }
        return liveData
    }

    fun disconnect() {
        resultLiveData?.removeObserver { observer }
    }

    abstract fun run(params: Params): LiveData<Result>
}