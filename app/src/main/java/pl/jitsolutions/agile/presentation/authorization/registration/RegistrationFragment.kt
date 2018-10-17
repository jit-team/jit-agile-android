package pl.jitsolutions.agile.presentation.authorization.registration

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
import pl.jitsolutions.agile.databinding.FragmentRegistrationBinding
import pl.jitsolutions.agile.presentation.BaseFragment

class RegistrationFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = RegistrationViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegistrationViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentRegistrationBinding>(inflater, R.layout.fragment_registration, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.registrationState.observe(this, Observer {
            if (it is RegistrationViewModel.RegistrationState.Success) {
                Toast.makeText(context, R.string.registration_screen_successful_toast_text, Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

}