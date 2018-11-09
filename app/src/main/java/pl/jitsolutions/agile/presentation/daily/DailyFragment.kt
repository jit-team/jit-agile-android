package pl.jitsolutions.agile.presentation.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentDailyBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class DailyFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDailyBinding>(
            layoutInflater,
            R.layout.fragment_daily,
            container,
            false
        )
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = DailyViewModel::class.java)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DailyViewModel::class.java)
        binding.viewModel = viewModel
        binding.adapter = DailyListAdapter {}
        binding.setLifecycleOwner(this)
        return binding.root
    }
}