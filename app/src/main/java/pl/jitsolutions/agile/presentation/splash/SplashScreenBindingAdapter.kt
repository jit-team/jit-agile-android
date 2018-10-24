package pl.jitsolutions.agile.presentation.splash

import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.jitsolutions.agile.R

@BindingAdapter("bindSplashVersion")
fun bindSplashVersion(view: TextView, version: String) {
    view.text = view.resources.getString(R.string.splash_screen_version, version)
}