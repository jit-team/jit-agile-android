package pl.jitsolutions.agile.presentation.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.login.LoginViewModel

@BindingAdapter("loginErrorText")
fun setLoginErrorText(textView: TextView, loginState: LoginViewModel.LoginState) {
    if (loginState == LoginViewModel.LoginState.Error) {
        textView.setText(R.string.login_screen_error_unknown)
    } else {
        textView.text = ""
    }
}