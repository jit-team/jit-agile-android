package pl.jitsolutions.agile.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.ActivityLoginBinding
import pl.jitsolutions.agile.presentation.BaseActivity
import pl.jitsolutions.agile.presentation.mainscreen.MainScreenActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory: ViewModelProvider.Factory by instance(tag = LoginViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.loginState.observe(this, Observer { loginState ->
            when (loginState) {
                is LoginViewModel.LoginState.None -> showToast("None")
                is LoginViewModel.LoginState.InProgress -> showToast("InProgress")
                is LoginViewModel.LoginState.Success -> showToast("Success")
                is LoginViewModel.LoginState.Error -> showToast("Error")
            }
        })
        viewModel.userName.observe(this, Observer { name ->
            if (!name.isEmpty()) {
                showToast(name)
                startActivity(Intent(this@LoginActivity, MainScreenActivity::class.java))
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(this@LoginActivity, "logged as: $text", Toast.LENGTH_SHORT).show()
    }
}