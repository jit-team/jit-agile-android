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
import pl.jitsolutions.agile.utils.OnClick
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.ActivityMainBinding
import pl.jitsolutions.agile.presentation.mainscreen.MainScreenActivity

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.onClick = onClick
    }

    private val onClick: OnClick = object : OnClick {
        override fun click() {
            viewModel.login().observe(this@LoginActivity, Observer {
                Toast.makeText(this@LoginActivity, "logged as: " + it.name , Toast.LENGTH_SHORT).show()
                this@LoginActivity.startActivity(Intent(this@LoginActivity, MainScreenActivity::class.java))
            })
        }
    }
}