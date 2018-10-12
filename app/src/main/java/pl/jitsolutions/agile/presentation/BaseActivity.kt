package pl.jitsolutions.agile.presentation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    private val appKodein by closestKodein()
    override val kodein = Kodein.lazy {
        extend(appKodein)
        bind<Context>() with provider { this@BaseActivity }
    }
    override val kodeinTrigger = KodeinTrigger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeinTrigger.trigger()
    }
}