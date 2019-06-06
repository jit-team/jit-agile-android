package pl.jitsolutions.agile.presentation.projects.details

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.with
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.DialogProjectDetailsChangePasswordBinding
import pl.jitsolutions.agile.di.Tags
import pl.jitsolutions.agile.presentation.common.BaseDialogFragment

class ChangeProjectPasswordDialogFragment : BaseDialogFragment() {

    override val fragmentModule = Kodein.Module("ChangeProjectPasswordDialogFragment") {
        constant(tag = Tags.Parameters.PROJECT_DETAILS_ID) with arguments!!["projectId"]!! as String
    }

    companion object {
        fun show(fragmentManager: FragmentManager, tag: String, projectId: String) {
            ChangeProjectPasswordDialogFragment().apply {
                arguments = Bundle().apply { putString("projectId", projectId) }
                isCancelable = false
                show(fragmentManager, tag)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = createBinding(it)
            val dialog = buildDialog(it, binding.root)
            val viewModel = getViewModel()

            binding.viewModel = viewModel
            binding.setLifecycleOwner(it)

            dialog.setOnShowListener {
                binding.dialog = dialog
                dialog.window
                    ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }

            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createBinding(it: FragmentActivity): DialogProjectDetailsChangePasswordBinding {
        return DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_project_details_change_password,
            null,
            false
        )
    }

    private fun buildDialog(
        it: FragmentActivity,
        view: View
    ): AlertDialog {
        return AlertDialog.Builder(it)
            .setTitle(R.string.project_details_screen_change_password_title)
            .setView(view)
            .setPositiveButton(
                R.string.project_details_screen_change_password_positive_button_text,
                null
            )
            .setNegativeButton(
                R.string.project_details_screen_change_password_negative_button_text,
                null
            )
            .create()
    }

    private fun getViewModel(): ChangeProjectPasswordViewModel {
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ChangeProjectPasswordViewModel::class.java)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChangeProjectPasswordViewModel::class.java)

        viewModel.state.observe(activity!!, Observer { state ->
            if (state == ChangeProjectPasswordViewModel.State.Success) {
                Toast.makeText(
                    activity,
                    R.string.project_details_screen_change_password_success_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return viewModel
    }
}