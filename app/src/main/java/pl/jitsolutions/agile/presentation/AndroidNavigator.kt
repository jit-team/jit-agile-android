package pl.jitsolutions.agile.presentation

import android.content.Context
import pl.jitsolutions.agile.presentation.mainscreen.launchMainActivity
import pl.jitsolutions.agile.presentation.register.launchRegisterActivity

class AndroidNavigator(private val context: Context) : Navigator {
    override fun goToRegistration() = launchRegisterActivity(context)
    override fun goToMain() = launchMainActivity(context)
}