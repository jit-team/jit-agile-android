package pl.jitsolutions.agile.presentation

import android.content.Context
import android.content.Intent
import pl.jitsolutions.agile.presentation.register.RegisterActivity

class AndroidNavigator(private val context: Context) : Navigator {
    override fun goToRegistration() {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }
}