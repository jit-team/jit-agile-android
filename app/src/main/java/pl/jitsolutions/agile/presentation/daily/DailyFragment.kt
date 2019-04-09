package pl.jitsolutions.agile.presentation.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.with
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentDailyBinding
import pl.jitsolutions.agile.di.Tags
import pl.jitsolutions.agile.presentation.common.BaseFragment

class DailyFragment : BaseFragment() {
    lateinit var viewModel: DailyViewModel

    override val fragmentModule = Kodein.Module("DailyFragment") {
        constant(tag = Tags.Parameters.DAILY_ID) with
            DailyFragmentArgs.fromBundle(arguments!!).dailyId
    }

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
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DailyViewModel::class.java)
        binding.viewModel = viewModel
        binding.adapter = DailyListAdapter()
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.dailyState.value == DailyViewModel.DailyState.End) {
            viewModel.quitDaily()
        } else {
            showDailyLeaveConfirmation(view!!) { viewModel.leaveDaily() }
        }
        return true
    }
}