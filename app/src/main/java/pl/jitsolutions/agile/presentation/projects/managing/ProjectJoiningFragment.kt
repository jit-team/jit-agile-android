package pl.jitsolutions.agile.presentation.projects.managing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
                showProjectJoiningSuccessfulDialog()
            }
        })
        return binding.root
    }

    private fun showProjectJoiningSuccessfulDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.project_joining_screen_dialog_user_has_been_joined_text)
                .setPositiveButton(R.string.project_joining_screen_dialog_positive_text) { dialog, _ ->
                    dialog.dismiss()
                    viewModel.confirmSuccess()
                }.show()
        }
    }
}