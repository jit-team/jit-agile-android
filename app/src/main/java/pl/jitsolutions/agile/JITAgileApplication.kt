package pl.jitsolutions.agile

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import pl.jitsolutions.agile.domain.GetCurrentUserUseCase
import pl.jitsolutions.agile.presentation.login.LoginViewModelFactory
import pl.jitsolutions.agile.repository.MockUserRepository
import pl.jitsolutions.agile.repository.UserRepository

class JITAgileApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        bind<UserRepository>() with singleton { MockUserRepository() }
        bind<GetCurrentUserUseCase>() with provider { GetCurrentUserUseCase(instance()) }
        bind<LoginViewModelFactory>() with provider { LoginViewModelFactory(instance()) }
    }
}