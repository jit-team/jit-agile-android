package pl.jitsolutions.agile.presentation.authorization.registrationSuccessful

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentRegistrationSuccessfulBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class RegistrationSuccessfulFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = RegistrationSuccessfulViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(RegistrationSuccessfulViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentRegistrationSuccessfulBinding>(
            layoutInflater,
            R.layout.fragment_registration_successful,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }
}