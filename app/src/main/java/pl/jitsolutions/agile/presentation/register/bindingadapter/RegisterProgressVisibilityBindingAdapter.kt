package pl.jitsolutions.agile.presentation.register.bindingadapter

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.presentation.register.RegisterViewModel

@BindingAdapter("registerProgressVisibility")
fun registerProgressVisibilityBindingAdapter(progressBar: ProgressBar, registerState: RegisterViewModel.RegisterState) {
    progressBar.visibility = when (registerState) {
        RegisterViewModel.RegisterState.None -> View.INVISIBLE
        RegisterViewModel.RegisterState.InProgress -> View.VISIBLE
        RegisterViewModel.RegisterState.Error -> View.INVISIBLE
        RegisterViewModel.RegisterState.Success -> View.INVISIBLE
    }
}
