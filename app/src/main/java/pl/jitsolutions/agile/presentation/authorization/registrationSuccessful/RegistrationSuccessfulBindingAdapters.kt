package pl.jitsolutions.agile.presentation.authorization.registrationSuccessful

import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

@BindingAdapter("bindRegistrationSuccessFulBackArrowVisibility")
fun registrationSuccessfulBackArrowVisibilityBindingAdapter(view : View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextThemeWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}