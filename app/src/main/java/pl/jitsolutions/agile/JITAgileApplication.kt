package pl.jitsolutions.agile

import android.app.Application
import org.kodein.di.KodeinAware
import pl.jitsolutions.agile.di.kodeinBuilder

class JITAgileApplication : Application(), KodeinAware {
    override val kodein = kodeinBuilder
}