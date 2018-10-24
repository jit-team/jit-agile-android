package pl.jitsolutions.agile.presentation.authorization.resetPassword

import android.content.ContextWrapper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import pl.jitsolutions.agile.R

@BindingAdapter("bindResetPasswordBackArrowVisibility")
fun resetPasswordBackArrowVisibility(view: View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}

@BindingAdapter("bindResetPasswordCredentialsEditingEnabled")
fun resetPasswordCredentialsEditingEnabled(view: View, resetPasswordState: ResetPasswordViewModel.ResetPasswordState) {
    view.isEnabled = when (resetPasswordState) {
        ResetPasswordViewModel.ResetPasswordState.None -> true
        ResetPasswordViewModel.ResetPasswordState.InProgress -> false
        is ResetPasswordViewModel.ResetPasswordState.Error -> true
        ResetPasswordViewModel.ResetPasswordState.Success -> false
    }
}

@BindingAdapter("bindResetPasswordEmailError")
fun resetPasswordEmailError(view: TextInputLayout, resetPasswordState: ResetPasswordViewModel.ResetPasswordState) {
    val errorText: String? = when {
        resetPasswordState.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.EMAIL) ->
            view.context.getString(R.string.reset_password_screen_error_invalid_email)
        resetPasswordState.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.EMAIL_NOT_FOUND) ->
            view.context.getString(R.string.reset_password_screen_error_email_not_found)
        else -> null
    }
    view.isErrorEnabled = errorText != null
    view.error = errorText
}


@BindingAdapter("bindResetPasswordProgressVisibility")
fun resetPasswordProgressVisibilityBindingAdapter(progressBar: ProgressBar, resetPasswordState: ResetPasswordViewModel.ResetPasswordState) {
    progressBar.visibility = when (resetPasswordState) {
        ResetPasswordViewModel.ResetPasswordState.None -> View.INVISIBLE
        ResetPasswordViewModel.ResetPasswordState.InProgress -> View.VISIBLE
        is ResetPasswordViewModel.ResetPasswordState.Error -> View.INVISIBLE
        ResetPasswordViewModel.ResetPasswordState.Success -> View.INVISIBLE
    }
}

@BindingAdapter("bindResetPasswordUnknownErrorVisibility")
fun bindResetPasswordUnknownErrorVisibility(view: TextView, resetPasswordState: ResetPasswordViewModel.ResetPasswordState) {
    val errorText: String? = when {
        resetPasswordState.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.SERVER) ->
            view.context.getString(R.string.reset_password_screen_error_network)
        resetPasswordState.isErrorOfType(ResetPasswordViewModel.ResetPasswordTypeError.UNKNOWN) ->
            view.context.getString(R.string.reset_password_screen_error_unknown)
        else -> null
    }
    view.text = errorText
    view.visibility = if (errorText != null) View.VISIBLE else View.INVISIBLE
}
