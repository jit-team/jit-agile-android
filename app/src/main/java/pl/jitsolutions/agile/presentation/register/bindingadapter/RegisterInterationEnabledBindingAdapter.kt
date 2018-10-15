package pl.jitsolutions.agile.presentation.register.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.register.RegisterViewModel

@BindingAdapter("android:enabled")
fun setRgisterCredentialsEditing(view: View, registerState: RegisterViewModel.RegisterState ) {
    view.isEnabled = when (registerState) {
        RegisterViewModel.RegisterState.None -> true
        RegisterViewModel.RegisterState.InProgress -> false
        RegisterViewModel.RegisterState.Error -> true
        RegisterViewModel.RegisterState.Success -> false
    }
}