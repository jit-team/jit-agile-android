package pl.jitsolutions.agile.presentation.projects.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.with
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.FragmentProjectDetailsBinding
import pl.jitsolutions.agile.di.Tags
import pl.jitsolutions.agile.presentation.common.BaseFragment

class ProjectDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentProjectDetailsBinding

    override val fragmentModule = Kodein.Module("ProjectDetailsFragment") {
        constant(tag = Tags.Parameters.PROJECT_DETAILS_ID) with
            ProjectDetailsFragmentArgs.fromBundle(arguments!!).projectId
        constant(tag = Tags.Parameters.PROJECT_DETAILS_ACTIVE) with
            ProjectDetailsFragmentArgs.fromBundle(arguments!!).active
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ProjectDetailsViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_project_details,
            container,
            false
        )
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProjectDetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.state.observe(this, Observer {
            setMenuVisibility(it !is ProjectDetailsViewModel.State.InProgress)
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_project_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item!!.itemId != android.R.id.home) {
            binding.menuItemId = item.itemId
            true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}