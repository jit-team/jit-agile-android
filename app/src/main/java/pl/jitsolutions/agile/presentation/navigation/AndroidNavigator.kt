package pl.jitsolutions.agile.presentation.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.daily.DailyFragmentDirections
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Daily
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Login
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectAdding
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectCreation
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectDetails
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectJoining
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ProjectList
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Registration
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.RegistrationSuccessful
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.ResetPassword
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.Splash
import pl.jitsolutions.agile.presentation.projects.ProjectListFragmentDirections

class AndroidNavigator(context: Context) : Navigator {
    private val observers = mutableMapOf<Int, MutableSet<Navigator.NavigationObserver>>()
    private val activity = context as AppCompatActivity
    private val navController by lazy {
        activity.findNavController(android.R.id.content).apply {
            addOnNavigatedListener { _, destination ->
                observers[destination.id]?.forEach { observer ->
                    Navigator.Destination.forId(destination.id)?.let {
                        observer.onNavigation(it)
                    }
                }
            }
        }
    }

    override fun addDestinationObserver(
        destination: Navigator.Destination,
        observer: Navigator.NavigationObserver
    ) {
        var observersForDestination = observers[destination.id]
        if (observersForDestination == null) {
            observersForDestination = mutableSetOf()
        }
        observersForDestination.add(observer)
        observers[destination.id] = observersForDestination
    }

    override fun removeDestinationObserver(
        destination: Navigator.Destination,
        observer: Navigator.NavigationObserver
    ) {
        val observersForDestination = observers[destination.id]
        if (observersForDestination != null) {
            observersForDestination.remove(observer)
            observers[destination.id] = observersForDestination
        }
    }

    override fun navigate(from: Navigator.Destination, to: Navigator.Destination) {
        when (from) {
            Splash -> when (to) {
                Login -> navController.navigate(R.id.action_splashFragment_to_loginFragment)
                ProjectList -> navController.navigate(R.id.action_splashFragment_to_projectListFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            Login -> when (to) {
                Registration -> navController.navigate(R.id.action_loginFragment_to_registrationFragment)
                ProjectList -> navController.navigate(R.id.action_loginFragment_to_projectListFragment)
                ResetPassword -> navController.navigate(R.id.action_loginFragment_to_resetFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            Registration -> when (to) {
                RegistrationSuccessful -> navController.navigate(R.id.action_registrationFragment_to_registrationSuccessfulFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            RegistrationSuccessful -> when (to) {
                ProjectList -> navController.navigate(R.id.action_registrationSuccessfulFragment_to_projectListFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            ProjectList -> when (to) {
                Login -> navController.navigate(R.id.action_projectListFragment_to_loginFragment)
                is ProjectDetails -> {
                    navController.navigate(
                        ProjectListFragmentDirections
                            .showProjectDetails()
                            .setProjectId(to.projectId)
                    )
                }
                is ProjectCreation -> {
                    navController.navigate(R.id.projectCreation)
                }
                is ProjectAdding -> {
                    navController.navigate(R.id.projectAdding)
                }
                is Daily -> {
                    navController.navigate(
                        DailyFragmentDirections.daily()
                            .setDailyId(to.dailyId)
                    )
                }
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            ResetPassword -> when (to) {
                Login -> navController.popBackStack()
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            is ProjectDetails -> when (to) {
                ProjectList -> navController.navigateUp()
                is Daily -> {
                    navController.navigate(
                        DailyFragmentDirections.daily()
                            .setDailyId(to.dailyId)
                    )
                }
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            ProjectCreation -> when (to) {
                ProjectList -> navController.popBackStack()
                is ProjectDetails -> {
                    navController.popBackStack()
                    navController.navigate(
                        ProjectListFragmentDirections
                            .showProjectDetails()
                            .setProjectId(to.projectId)
                    )
                }
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            ProjectJoining -> when (to) {
                ProjectList -> navController.popBackStack()
                is ProjectDetails -> {
                    navController.popBackStack()
                    navController.navigate(
                        ProjectListFragmentDirections
                            .showProjectDetails()
                            .setProjectId(to.projectId)
                    )
                }
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
        }
    }

    override fun navigateBack(from: Navigator.Destination?): Boolean {
        return when (from) {
            RegistrationSuccessful -> {
                navigate(RegistrationSuccessful, ProjectList)
                true
            }
            ResetPassword -> {
                navigate(ResetPassword, Login)
                true
            }
            is ProjectDetails -> {
                navigate(from, ProjectList)
                true
            }
            ProjectCreation -> {
                navigate(ProjectCreation, ProjectList)
                true
            }
            ProjectJoining -> {
                navigate(ProjectJoining, ProjectList)
                true
            }
            is Daily -> {
                navController.navigateUp()
                true
            }
            else -> false
        }
    }

    override fun forceFinish() {
        activity.finish()
    }
}