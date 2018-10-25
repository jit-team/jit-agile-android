package pl.jitsolutions.agile.presentation.projects.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentProjectDetailsBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class ProjectDetailsFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ProjectDetailsViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentProjectDetailsBinding>(inflater, R.layout.fragment_project_details, container, false)
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectDetailsViewModel::class.java)
        binding.setLifecycleOwner(this)
        return binding.root
    }
}