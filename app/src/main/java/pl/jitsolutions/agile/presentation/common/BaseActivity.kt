package pl.jitsolutions.agile.presentation.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.presentation.navigation.AndroidNavigator
import pl.jitsolutions.agile.presentation.navigation.Navigator

abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    abstract val navigationGraphResId: Int
    private val appKodein by closestKodein()
    override val kodein = Kodein.lazy {
        extend(appKodein)
        bind<Context>() with singleton { this@BaseActivity }
        bind<Navigator>() with singleton { AndroidNavigator(instance()) }
    }
    override val kodeinTrigger = KodeinTrigger()

    private val navigator: Navigator by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodeinTrigger.trigger()

        registerNotificationChannel(this)

        val navHost = NavHostFragment.create(navigationGraphResId)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, navHost, "NavHostFragment")
            .setPrimaryNavigationFragment(navHost)
            .commit()
    }

    override fun onSupportNavigateUp() = findNavController(this, android.R.id.content).navigateUp()

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentByTag("NavHostFragment")!!
        val topFragment = navHost.childFragmentManager.fragments.last() as FragmentNavigation

        if (topFragment.onBackPressed()) {
            return
        }

        if (!navigator.navigateBack(topFragment.destination)) {
            super.onBackPressed()
        }
    }

    private fun registerNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = context.getString(R.string.notification_channel_id)
            val channelName = context.getString(R.string.notification_channel_name)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }
}