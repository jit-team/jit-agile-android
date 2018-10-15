package pl.jitsolutions.agile.presentation.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.ActivityRegisterBinding
import pl.jitsolutions.agile.presentation.BaseActivity

fun launchRegisterActivity(context: Context) {
    context.startActivity(Intent(context, RegisterActivity::class.java))
}

class RegisterActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory: ViewModelProvider.Factory by instance(tag = RegisterViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.registerState.observe(this, Observer {
            when (it) {
                RegisterViewModel.RegisterState.Success -> {
                    Toast.makeText(this@RegisterActivity, R.string.register_successful_toast_text, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })
    }
}