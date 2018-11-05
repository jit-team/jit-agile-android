package pl.jitsolutions.agile.presentation.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentProjectListBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class ProjectListFragment : BaseFragment() {
    lateinit var binding: FragmentProjectListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ProjectListViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ProjectListViewModel::class.java)
        binding.viewModel = viewModel

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)
        binding.adapter =
            ProjectListAdapter { project -> viewModel.showProjectDetails(project.id) }
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        binding.menuItemId = item?.itemId ?: -1
        return true
    }
}