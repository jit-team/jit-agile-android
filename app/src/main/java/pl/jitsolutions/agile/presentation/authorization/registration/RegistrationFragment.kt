package pl.jitsolutions.agile.presentation.authorization.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentRegistrationBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class RegistrationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = RegistrationViewModel::class.java)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RegistrationViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentRegistrationBinding>(
            inflater,
            R.layout.fragment_registration,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }
}