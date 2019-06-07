package pl.jitsolutions.agile.presentation.navigation

import pl.jitsolutions.agile.R

interface Navigator {
    fun navigate(from: Destination, to: Destination)

    fun navigateBack(from: Destination?): Boolean

    fun forceFinish()

    fun addDestinationObserver(destination: Destination, observer: NavigationObserver)

    fun removeDestinationObserver(destination: Destination, observer: NavigationObserver)

    sealed class Destination(val id: Int) {
        object Splash : Destination(R.id.splashFragment)
        object Login : Destination(R.id.loginFragment)
        object ResetPassword : Destination(R.id.resetPasswordFragment)
        object Registration : Destination(R.id.registrationFragment)
        object RegistrationSuccessful : Destination(R.id.registrationSuccessfulFragment)
        object ProjectList : Destination(R.id.projectListFragment)
        class ProjectDetails(val projectId: String, val active: Boolean) : Destination(R.id.projectDetailsFragment)
        object ChangeProjectPassword : Destination(R.id.changeProjectPasswordDialogFragment)
        object ProjectCreation : Destination(R.id.projectCreation)
        object ProjectAdding : Destination(R.id.projectAdding)
        object ProjectJoining : Destination(R.id.projectCreation)
        class Daily(val dailyId: String) : Destination(R.id.dailyFragment)
        object PlanningPoker : Destination(R.id.planningPoker)

        companion object {
            fun forId(id: Int): Destination? {
                return when (id) {
                    R.id.splashFragment -> Splash
                    R.id.loginFragment -> Login
                    R.id.resetPasswordFragment -> ResetPassword
                    R.id.registrationFragment -> Registration
                    R.id.registrationSuccessfulFragment -> RegistrationSuccessful
                    R.id.projectListFragment -> ProjectList
                    R.id.projectDetailsFragment -> ProjectDetails("", false)
                    R.id.projectCreation -> ProjectCreation
                    R.id.projectAdding -> ProjectAdding
                    R.id.dailyFragment -> Daily("")
                    R.id.changeProjectPasswordDialogFragment -> ChangeProjectPassword
                    else -> null
                }
            }
        }
    }

    class InvalidNavigationException(from: Destination, to: Destination) :
        Exception("Invalid navigation from $from to $to")

    interface NavigationObserver {
        fun onNavigation(destination: Destination)
    }
}