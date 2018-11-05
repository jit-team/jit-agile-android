package pl.jitsolutions.agile.presentation.projects.managing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.generic.instance
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.common.BaseFragment
import pl.jitsolutions.agile.databinding.FragmentNewProjectBinding

class NewProjectFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = NewProjectViewModel::class.java)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(NewProjectViewModel::class.java)
        val binding: FragmentNewProjectBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_project, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }
}
