package pl.jitsolutions.agile.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentSplashBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment
import pl.jitsolutions.agile.presentation.navigation.Navigator

class SplashFragment : BaseFragment() {

    lateinit var viewModel: SplashViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = SplashViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentSplashBinding>(inflater, R.layout.fragment_splash, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.splashFinished()
    }
}