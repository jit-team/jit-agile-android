package pl.jitsolutions.agile.presentation.projects.managing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentProjectAddingBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class ProjectAddingFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProjectAddingBinding>(
            layoutInflater,
            R.layout.fragment_project_adding,
            container,
            false
        )
        return binding.root
    }
}
