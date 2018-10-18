package pl.jitsolutions.agile.presentation.common

import android.content.Context
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.x.closestKodein

abstract class BaseFragment : Fragment(), KodeinAware {
    override val kodein by closestKodein()
    override val kodeinTrigger = KodeinTrigger()

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        kodeinTrigger.trigger()
    }
}