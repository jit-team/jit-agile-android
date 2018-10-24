package pl.jitsolutions.agile.presentation.authorization.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentResetPasswordBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment
import pl.jitsolutions.agile.presentation.navigation.Navigator

class ResetPasswordFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        destination = Navigator.Destination.RESET_PASSWORD
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ResetPasswordViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ResetPasswordViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentResetPasswordBinding>(layoutInflater, R.layout.fragment_reset_password, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.resetPasswordState.observe(this, Observer {
            if (it is ResetPasswordViewModel.ResetPasswordState.Success) {
                Toast.makeText(this@ResetPasswordFragment.context, R.string.reset_password_screen_toast_email_sent, Toast.LENGTH_LONG).show()
            }
        })
        return binding.root
    }

}
