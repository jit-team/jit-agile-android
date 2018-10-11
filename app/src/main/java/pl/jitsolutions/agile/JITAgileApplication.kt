package pl.jitsolutions.agile

import android.app.Application
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.presentation.login.LoginViewModelFactory
import pl.jitsolutions.agile.repository.*

class JITAgileApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        bind<CoroutineDispatcher>() with singleton { Dispatchers.IO }
        bind<FirebaseUserRepository>() with singleton { FirebaseUserRepository(instance()) }
        bind<MockProjectRepository>() with singleton { MockProjectRepository(instance()) }
        bind<LoginUserUseCase>() with provider { LoginUserUseCase(instance(), instance(), instance()) }
        bind<LoginViewModelFactory>() with provider { LoginViewModelFactory(instance()) }
    }
}