package pl.jitsolutions.agile.presentation.navigation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.navigation.Navigator.Destination.*
import pl.jitsolutions.agile.presentation.projects.ProjectListFragmentDirections

class AndroidNavigator(context: Context) : Navigator {
    private val activity = context as AppCompatActivity

    override fun navigate(from: Navigator.Destination, to: Navigator.Destination, arguments: Bundle?) {
        val navController = activity.findNavController(android.R.id.content)
        when (from) {
            SPLASH -> when (to) {
                LOGIN -> navController.navigate(R.id.action_splashFragment_to_loginFragment)
                PROJECT_LIST -> navController.navigate(R.id.action_splashFragment_to_projectListFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            LOGIN -> when (to) {
                REGISTRATION -> navController.navigate(R.id.action_loginFragment_to_registrationFragment)
                PROJECT_LIST -> navController.navigate(R.id.action_loginFragment_to_projectListFragment)
                RESET_PASSWORD -> navController.navigate(R.id.action_loginFragment_to_resetFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            REGISTRATION -> when (to) {
                REGISTRATION_SUCCESSFUL -> navController.navigate(R.id.action_registrationFragment_to_registrationSuccessfulFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            REGISTRATION_SUCCESSFUL -> when (to) {
                PROJECT_LIST -> navController.navigate(R.id.action_registrationSuccessfulFragment_to_projectListFragment)
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            PROJECT_LIST -> when (to) {
                LOGIN -> navController.navigate(R.id.action_projectListFragment_to_loginFragment)
                PROJECT_DETAILS -> {
                    navController.navigate(ProjectListFragmentDirections
                            .actionProjectListFragmentToProjectDetailsFragment()
                            .setProjectId("exempleId")
                    )
                }
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            RESET_PASSWORD -> when (to) {
                LOGIN -> navController.popBackStack()
                else -> throw Navigator.InvalidNavigationException(from, to)
            }
            PROJECT_DETAILS -> throw Navigator.InvalidNavigationException(from, to)
        }
    }

    override fun navigateBack(from: Navigator.Destination?): Boolean {
        return when (from) {
            REGISTRATION_SUCCESSFUL -> {
                navigate(REGISTRATION_SUCCESSFUL, PROJECT_LIST)
                true
            }
            RESET_PASSWORD -> {
                navigate(RESET_PASSWORD, LOGIN)
                true
            }
            else -> false
        }
    }
}