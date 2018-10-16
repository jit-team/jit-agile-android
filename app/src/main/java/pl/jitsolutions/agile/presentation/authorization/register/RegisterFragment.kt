package pl.jitsolutions.agile.presentation.authorization.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentRegisterBinding
import pl.jitsolutions.agile.presentation.BaseFragment

class RegisterFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = RegisterViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = viewModel
        return binding.root
    }
}