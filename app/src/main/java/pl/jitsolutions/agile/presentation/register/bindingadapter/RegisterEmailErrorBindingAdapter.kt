package pl.jitsolutions.agile.presentation.register.bindingadapter


import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("registerEmailError")
fun registerEmailErrorBindingAdapter(view: TextInputLayout, errorMessage: String) {
    view.isErrorEnabled = !errorMessage.isEmpty()
    view.error = errorMessage
}
