package pl.jitsolutions.agile.presentation.mainscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun launchMainActivity(context: Context) {
    context.startActivity(Intent(context, MainScreenActivity::class.java))
}

class MainScreenActivity : AppCompatActivity() {

}