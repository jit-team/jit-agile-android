package pl.jitsolutions.agile.presentation.authorization.resetPassword

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
import pl.jitsolutions.agile.databinding.FragmentResetPasswordBinding
import pl.jitsolutions.agile.presentation.common.BaseFragment
import pl.jitsolutions.agile.presentation.navigation.Navigator

class ResetPasswordFragment : BaseFragment() {

    lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        destination = Navigator.Destination.ResetPassword
        val viewModelFactory: ViewModelProvider.Factory by instance(tag = ResetPasswordViewModel::class.java)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ResetPasswordViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentResetPasswordBinding>(
            layoutInflater,
            R.layout.fragment_reset_password,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        viewModel.state.observe(this, Observer {
            if (it is ResetPasswordViewModel.State.Success) {
                showResetPasswordSuccessfulDialog()
            }
        })
        return binding.root
    }

    private fun showResetPasswordSuccessfulDialog() {
        this@ResetPasswordFragment.context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.reset_password_screen_dialog_email_sent_text)
                .setPositiveButton(R.string.reset_password_screen_dialog_positive_text) { dialog, _ ->
                    dialog.dismiss()
                    viewModel.confirmSuccess()
                }.show()
        }
    }
}
