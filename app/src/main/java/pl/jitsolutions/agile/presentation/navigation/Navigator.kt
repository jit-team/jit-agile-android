package pl.jitsolutions.agile.presentation.navigation

import android.os.Bundle

interface Navigator {
    fun navigate(from: Destination, to: Destination, arguments: Bundle? = null)

    enum class Destination { SPLASH, LOGIN, REGISTRATION, REGISTRATION_SUCCESSFUL, PROJECT_LIST }

    class InvalidNavigationException(from: Destination, to: Destination)
        : Exception("Invalid navigation from $from to $to")
}