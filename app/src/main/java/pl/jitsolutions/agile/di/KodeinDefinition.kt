package pl.jitsolutions.agile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import pl.jitsolutions.agile.domain.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.LoginUserUseCase
import pl.jitsolutions.agile.domain.UserRegistrationUseCase
import pl.jitsolutions.agile.presentation.authorization.login.LoginViewModel
import pl.jitsolutions.agile.presentation.authorization.registration.RegistrationViewModel
import pl.jitsolutions.agile.presentation.authorization.registrationSuccessful.RegistrationSuccessfulViewModel
import pl.jitsolutions.agile.presentation.navigation.AndroidNavigator
import pl.jitsolutions.agile.presentation.navigation.Navigator
import pl.jitsolutions.agile.presentation.projects.ProjectListViewModel
import pl.jitsolutions.agile.presentation.splash.SplashViewModel
import pl.jitsolutions.agile.repository.FirebaseUserRepository
import pl.jitsolutions.agile.repository.MockProjectRepository
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.UserRepository
import java.util.concurrent.Executors

interface Tags {
    enum class Dispatchers { USE_CASE, IO, MAIN }
}

private val dispatchersModule = Kodein.Module(name = "Dispatchers") {
    bind<CoroutineDispatcher>(tag = Tags.Dispatchers.USE_CASE) with singleton {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }
    bind<CoroutineDispatcher>(tag = Tags.Dispatchers.IO) with singleton {
        Dispatchers.IO
    }
    bind<CoroutineDispatcher>(tag = Tags.Dispatchers.MAIN) with singleton {
        Dispatchers.Main
    }
}

private val repositoriesModule = Kodein.Module(name = "Repositories") {
    bind<UserRepository>() with singleton {
        FirebaseUserRepository(instance(tag = Tags.Dispatchers.IO))
    }
    bind<ProjectRepository>() with singleton {
        MockProjectRepository(instance(tag = Tags.Dispatchers.IO))
    }
}

private val useCasesModule = Kodein.Module(name = "UseCases") {
    bind<UserRegistrationUseCase>() with provider {
        UserRegistrationUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<LoginUserUseCase>() with provider {
        LoginUserUseCase(instance(), instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<GetLoggedUserUseCase>() with provider {
        GetLoggedUserUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
}

private val viewModelsModule = Kodein.Module(name = "ViewModels") {
    bind<ViewModelProvider.Factory>(tag = SplashViewModel::class.java) with provider {
        viewModelFactory { SplashViewModel(instance(), instance(), instance(tag = Tags.Dispatchers.MAIN)) }
    }
    bind<ViewModelProvider.Factory>(tag = RegistrationViewModel::class.java) with provider {
        viewModelFactory { RegistrationViewModel(instance(), instance(), instance(tag = Tags.Dispatchers.MAIN)) }
    }
    bind<ViewModelProvider.Factory>(tag = LoginViewModel::class.java) with provider {
        viewModelFactory { LoginViewModel(instance(), instance(), instance(tag = Tags.Dispatchers.MAIN)) }
    }
    bind<ViewModelProvider.Factory>(tag = RegistrationSuccessfulViewModel::class.java) with provider {
        viewModelFactory { RegistrationSuccessfulViewModel(instance(), instance(), instance(tag = Tags.Dispatchers.MAIN)) }
    }
    bind<ViewModelProvider.Factory>(tag = ProjectListViewModel::class.java) with provider {
        viewModelFactory { ProjectListViewModel(instance(), instance(), instance(tag = Tags.Dispatchers.MAIN)) }
    }
}

//TODO: move to utils file or something
private fun viewModelFactory(factory: () -> ViewModel): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory.invoke() as T
        }
    }
}

val kodeinBuilder = Kodein.lazy {
    import(dispatchersModule)
    import(repositoriesModule)
    import(useCasesModule)
    import(viewModelsModule)

    bind<Navigator>() with provider { AndroidNavigator(instance()) }
}