package pl.jitsolutions.agile.presentation.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentProjectListBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment
import pl.jitsolutions.agile.presentation.navigation.Navigator

class ProjectListFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ProjectListViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectListViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentProjectListBinding>(inflater, R.layout.fragment_project_list, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }
}