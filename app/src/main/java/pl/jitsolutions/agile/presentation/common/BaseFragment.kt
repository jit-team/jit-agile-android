package pl.jitsolutions.agile.presentation.common

import android.content.Context
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.x.closestKodein
import pl.jitsolutions.agile.presentation.navigation.Navigator

abstract class BaseFragment : Fragment(), KodeinAware {
    var destination: Navigator.Destination? = null
    private val closestKodein by closestKodein()
    override val kodein = Kodein.lazy {
        extend(closestKodein)
        fragmentModule?.let { import(it) }
    }
    override val kodeinTrigger = KodeinTrigger()
    open val fragmentModule: Kodein.Module? = null

    @CallSuper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        kodeinTrigger.trigger()
    }

    open fun onBackPressed() : Boolean {
        return false
    }
}