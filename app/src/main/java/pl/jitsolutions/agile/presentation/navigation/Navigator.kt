package pl.jitsolutions.agile.presentation.navigation

interface Navigator {
    fun navigate(from: Destination, to: Destination)

    fun navigateBack(from: Destination?): Boolean

    sealed class Destination {
        object Splash : Destination()
        object Login : Destination()
        object ResetPassword : Destination()
        object Registration : Destination()
        object RegistrationSuccessful : Destination()
        object ProjectList : Destination()
        class ProjectDetails(val projectId: String) : Destination()
    }

    class InvalidNavigationException(from: Destination, to: Destination) :
        Exception("Invalid navigation from $from to $to")
}