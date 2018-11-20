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
import pl.jitsolutions.agile.databinding.FragmentProjectJoiningBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment

class ProjectJoiningFragment : BaseFragment() {

    lateinit var viewModel: ProjectJoiningViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ProjectJoiningViewModel::class.java)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ProjectJoiningViewModel::class.java)
        val binding: FragmentProjectJoiningBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_joining, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.projectJoiningState.observe(this, Observer {
            if (it == ProjectJoiningViewModel.ProjectJoiningState.Success) {
                Toast.makeText(
                    context,
                    R.string.project_joining_screen_success_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return binding.root
    }
}