package pl.jitsolutions.agile.presentation.projects.managing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentProjectCreationBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class ProjectCreationFragment : BaseFragment() {

    lateinit var viewModel: ProjectCreationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ProjectCreationViewModel::class.java)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ProjectCreationViewModel::class.java)
        val binding: FragmentProjectCreationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_creation, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.projectCreationState.observe(this, Observer {
            if (it == ProjectCreationViewModel.ProjectCreationState.Success) {
                Toast.makeText(
                    context,
                    R.string.project_creation_screen_success_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return binding.root
    }
}
