package pl.jitsolutions.agile.presentation.common

import android.content.Context
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.x.closestKodein
import pl.jitsolutions.agile.presentation.navigation.Navigator

abstract class BaseFragment : Fragment(), KodeinAware {
    abstract val destination: Navigator.Destination
    override val kodein by closestKodein()
    override val kodeinTrigger = KodeinTrigger()

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        kodeinTrigger.trigger()
    }
}