package pl.jitsolutions.agile.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.mainscreen.launchMainActivity

class AndroidNavigator(context: Context) : Navigator {

    private val activity = context as AppCompatActivity

    override fun goToRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun goToMain() {
        activity.finish()
        launchMainActivity(activity)
    }

    override fun goToRegistrationSuccessful() {
        findNavController().navigate(R.id.action_registrationFragment_to_registrationSuccessfulFragment)
    }

    private fun findNavController() = activity.findNavController(android.R.id.content)
}