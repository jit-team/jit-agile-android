package pl.jitsolutions.agile.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.ActivityLoginBinding
import pl.jitsolutions.agile.presentation.mainscreen.MainScreenActivity
import pl.jitsolutions.agile.presentation.register.RegisterActivity

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: LoginViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.viewModel = viewModel

        viewModel.loginState.observe(this, Observer { loginState ->
            when (loginState) {
                is LoginState.None -> showToast("None")
                is LoginState.InProgress -> showToast("InProgress")
                is LoginState.Success -> showToast("Success")
                is LoginState.Error -> showToast("Error")
            }
        })
        viewModel.userName.observe(this, Observer { name ->
            if (!name.isEmpty()) {
                showToast(name)
                startActivity(Intent(this@LoginActivity, MainScreenActivity::class.java))
            }
        })
        viewModel.register.observe(this, Observer{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(this@LoginActivity, "logged as: $text", Toast.LENGTH_SHORT).show()
    }
}