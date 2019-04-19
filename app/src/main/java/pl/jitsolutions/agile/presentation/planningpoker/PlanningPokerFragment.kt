package pl.jitsolutions.agile.presentation.planningpoker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentPlanningPokerBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class PlanningPokerFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentPlanningPokerBinding>(
                inflater,
                R.layout.fragment_planning_poker,
                container,
                false
            )
        binding.adapter = PokerCardAdapter()
        binding.lifecycleOwner = this
        return binding.root
    }
}