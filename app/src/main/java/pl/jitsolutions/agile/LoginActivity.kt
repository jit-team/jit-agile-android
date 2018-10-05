package pl.jitsolutions.agile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.databinding.ActivityMainBinding
import pl.jitsolutions.agile.presentation.login.LoginViewModel
import pl.jitsolutions.agile.presentation.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: LoginViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = viewModel
    }
}
