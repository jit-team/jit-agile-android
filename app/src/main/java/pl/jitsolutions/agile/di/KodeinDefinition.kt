package pl.jitsolutions.agile.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Module
import org.kodein.di.LazyKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import pl.jitsolutions.agile.domain.usecases.AssignDeviceTokenToUserTokenUseCase
import pl.jitsolutions.agile.domain.usecases.ChangeProjectPasswordUseCase
import pl.jitsolutions.agile.domain.usecases.DeleteProjectUseCase
import pl.jitsolutions.agile.domain.usecases.EndDailyUseCase
import pl.jitsolutions.agile.domain.usecases.GetApplicationVersionUseCase
import pl.jitsolutions.agile.domain.usecases.GetCurrentUserProjectsUseCase
import pl.jitsolutions.agile.domain.usecases.GetCurrentUserProjectsWithDailyUseCase
import pl.jitsolutions.agile.domain.usecases.GetLoggedUserUseCase
import pl.jitsolutions.agile.domain.usecases.GetProjectUseCase
import pl.jitsolutions.agile.domain.usecases.JoinDailyUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveDailyUseCase
import pl.jitsolutions.agile.domain.usecases.LeaveProjectUseCase
import pl.jitsolutions.agile.domain.usecases.LogoutCurrentUserUseCase
import pl.jitsolutions.agile.domain.usecases.NextDailyUserUseCase
import pl.jitsolutions.agile.domain.usecases.ObserveDailyUseCase
import pl.jitsolutions.agile.domain.usecases.ProjectCreationUseCase
import pl.jitsolutions.agile.domain.usecases.ProjectJoiningUseCase
import pl.jitsolutions.agile.domain.usecases.StartDailyUseCase
import pl.jitsolutions.agile.domain.usecases.UserLoginUseCase
import pl.jitsolutions.agile.domain.usecases.UserRegistrationUseCase
import pl.jitsolutions.agile.domain.usecases.UserResetPasswordUseCase
import pl.jitsolutions.agile.presentation.authorization.login.LoginViewModel
import pl.jitsolutions.agile.presentation.authorization.registration.RegistrationViewModel
import pl.jitsolutions.agile.presentation.authorization.registrationSuccessful.RegistrationSuccessfulViewModel
import pl.jitsolutions.agile.presentation.authorization.resetPassword.ResetPasswordViewModel
import pl.jitsolutions.agile.presentation.daily.DailyViewModel
import pl.jitsolutions.agile.presentation.projects.ProjectListViewModel
import pl.jitsolutions.agile.presentation.projects.details.ChangeProjectPasswordViewModel
import pl.jitsolutions.agile.presentation.projects.details.ProjectDetailsViewModel
import pl.jitsolutions.agile.presentation.projects.managing.ProjectCreationViewModel
import pl.jitsolutions.agile.presentation.projects.managing.ProjectJoiningViewModel
import pl.jitsolutions.agile.presentation.splash.SplashViewModel
import pl.jitsolutions.agile.repository.AndroidSystemInfoRepository
import pl.jitsolutions.agile.repository.DailyRepository
import pl.jitsolutions.agile.repository.NotificationRepository
import pl.jitsolutions.agile.repository.ProjectRepository
import pl.jitsolutions.agile.repository.SystemInfoRepository
import pl.jitsolutions.agile.repository.UserRepository
import pl.jitsolutions.agile.repository.firebase.FirebaseDailyRepository
import pl.jitsolutions.agile.repository.firebase.FirebaseNotificationRepository
import pl.jitsolutions.agile.repository.firebase.FirebaseProjectRepository
import pl.jitsolutions.agile.repository.firebase.FirebaseUserRepository
import java.util.concurrent.Executors

interface Tags {
    enum class Dispatchers { USE_CASE, IO, MAIN }
    enum class Parameters { PROJECT_DETAILS_ID, PROJECT_DETAILS_ACTIVE, PROJECT_DETAILS_PASSWORD, DAILY_ID }
}

private val dispatchersModule = Module(name = "Dispatchers") {
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

private val repositoriesModule = Module(name = "Repositories") {
    bind<UserRepository>() with singleton {
        FirebaseUserRepository(instance(), instance(tag = Tags.Dispatchers.IO))
    }
    bind<ProjectRepository>() with singleton {
        FirebaseProjectRepository(instance(), instance(tag = Tags.Dispatchers.IO))
    }
    bind<DailyRepository>() with singleton {
        FirebaseDailyRepository(instance(), instance(), instance(tag = Tags.Dispatchers.IO))
    }
    bind<SystemInfoRepository>() with singleton {
        AndroidSystemInfoRepository(instance())
    }
    bind<NotificationRepository>() with singleton {
        FirebaseNotificationRepository(instance(), instance(tag = Tags.Dispatchers.IO))
    }
}

private val useCasesModule = Module(name = "UseCases") {
    bind<UserRegistrationUseCase>() with provider {
        UserRegistrationUseCase(instance(), instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<UserLoginUseCase>() with provider {
        UserLoginUseCase(instance(), instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<LogoutCurrentUserUseCase>() with provider {
        LogoutCurrentUserUseCase(instance(), instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<GetLoggedUserUseCase>() with provider {
        GetLoggedUserUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<GetApplicationVersionUseCase>() with provider {
        GetApplicationVersionUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<UserResetPasswordUseCase>() with provider {
        UserResetPasswordUseCase(instance(), instance(Tags.Dispatchers.USE_CASE))
    }
    bind<GetProjectUseCase>() with provider {
        GetProjectUseCase(instance(), instance(Tags.Dispatchers.USE_CASE))
    }
    bind<GetCurrentUserProjectsUseCase>() with provider {
        GetCurrentUserProjectsUseCase(instance(), instance(), instance(Tags.Dispatchers.USE_CASE))
    }
    bind<GetCurrentUserProjectsWithDailyUseCase>() with provider {
        GetCurrentUserProjectsWithDailyUseCase(
            instance(),
            instance(),
            instance(Tags.Dispatchers.USE_CASE)
        )
    }
    bind<LeaveProjectUseCase>() with provider {
        LeaveProjectUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<ProjectCreationUseCase>() with provider {
        ProjectCreationUseCase(instance(), instance(Tags.Dispatchers.USE_CASE))
    }
    bind<DeleteProjectUseCase>() with provider {
        DeleteProjectUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<ProjectJoiningUseCase>() with provider {
        ProjectJoiningUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<EndDailyUseCase>() with provider {
        EndDailyUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<JoinDailyUseCase>() with provider {
        JoinDailyUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<LeaveDailyUseCase>() with provider {
        LeaveDailyUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<StartDailyUseCase>() with provider {
        StartDailyUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<ObserveDailyUseCase>() with provider {
        ObserveDailyUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<NextDailyUserUseCase>() with provider {
        NextDailyUserUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
    bind<AssignDeviceTokenToUserTokenUseCase>() with provider {
        AssignDeviceTokenToUserTokenUseCase(
            instance(),
            instance(),
            instance(tag = Tags.Dispatchers.USE_CASE)
        )
    }
    bind<ChangeProjectPasswordUseCase>() with provider {
        ChangeProjectPasswordUseCase(instance(), instance(tag = Tags.Dispatchers.USE_CASE))
    }
}

private val viewModelsModule = Module(name = "ViewModels") {
    bind<ViewModelProvider.Factory>(tag = SplashViewModel::class.java) with provider {
        viewModelFactory {
            SplashViewModel(
                instance(),
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = RegistrationViewModel::class.java) with provider {
        viewModelFactory {
            RegistrationViewModel(
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = LoginViewModel::class.java) with provider {
        viewModelFactory {
            LoginViewModel(
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = RegistrationSuccessfulViewModel::class.java) with provider {
        viewModelFactory {
            RegistrationSuccessfulViewModel(
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = ProjectListViewModel::class.java) with provider {
        viewModelFactory {
            ProjectListViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = ResetPasswordViewModel::class.java) with provider {
        viewModelFactory {
            ResetPasswordViewModel(
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = ProjectDetailsViewModel::class.java) with provider {
        viewModelFactory {
            ProjectDetailsViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(Tags.Parameters.PROJECT_DETAILS_ID),
                instance(Tags.Parameters.PROJECT_DETAILS_ACTIVE),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = ProjectCreationViewModel::class.java) with provider {
        viewModelFactory {
            ProjectCreationViewModel(
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = ProjectJoiningViewModel::class.java) with provider {
        viewModelFactory {
            ProjectJoiningViewModel(
                instance(),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = DailyViewModel::class.java) with provider {
        viewModelFactory {
            DailyViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = Tags.Parameters.DAILY_ID),
                instance(),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
    bind<ViewModelProvider.Factory>(tag = ChangeProjectPasswordViewModel::class.java) with provider {
        viewModelFactory {
            ChangeProjectPasswordViewModel(
                instance(),
                instance(),
                instance(Tags.Parameters.PROJECT_DETAILS_ID),
                instance(tag = Tags.Dispatchers.MAIN)
            )
        }
    }
}

// TODO: move to utils file or something
private fun viewModelFactory(factory: () -> ViewModel): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory.invoke() as T
        }
    }
}

fun kodeinBuilder(application: Application): LazyKodein {
    return Kodein.lazy {
        bind<Application>() with provider {
            application
        }
        import(dispatchersModule)
        import(repositoriesModule)
        import(useCasesModule)
        import(viewModelsModule)
        import(firebaseModule)
    }
}