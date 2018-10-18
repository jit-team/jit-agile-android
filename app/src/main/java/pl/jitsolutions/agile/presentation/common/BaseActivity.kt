package pl.jitsolutions.agile.presentation.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    abstract val navigationGraphResId: Int
    private val appKodein by closestKodein()
    override val kodein = Kodein.lazy {
        extend(appKodein)
        bind<Context>() with provider { this@BaseActivity }
    }
    override val kodeinTrigger = KodeinTrigger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeinTrigger.trigger()

        val navHost = NavHostFragment.create(navigationGraphResId)
        supportFragmentManager.beginTransaction()
                .add(android.R.id.content, navHost)
                .setPrimaryNavigationFragment(navHost)
                .commit()
    }

    override fun onSupportNavigateUp() = findNavController(this, android.R.id.content).navigateUp()
}