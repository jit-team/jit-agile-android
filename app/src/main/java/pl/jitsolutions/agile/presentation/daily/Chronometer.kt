package pl.jitsolutions.agile.presentation.daily

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.widget.TextView
import pl.jitsolutions.agile.R

private const val DAILY_LENGTH = 15 * 60 * 1000L

class Chronometer(context: Context, attributes: AttributeSet) : TextView(
    context,
    attributes
) {

    private var based = 0L
    private val timeElapsed = StringBuilder()
    private var running = false
    private var started = false

    init {
        text = context.getString(R.string.daily_screen_duration_label)
    }

    fun setBase(dailyStartInMillis: Long) {
        based = dailyStartInMillis + DAILY_LENGTH
    }

    fun start() {
        started = true
        updateRunning()
    }

    fun stop() {
        started = false
        updateRunning()
        val dailyLength = System.currentTimeMillis() - based + DAILY_LENGTH
        val seconds = dailyLength / 1000
        text = DateUtils.formatElapsedTime(timeElapsed, seconds)
    }

    private fun updateText(now: Long) {
        val seconds = (based - now) / 1000
        text = DateUtils.formatElapsedTime(timeElapsed, seconds)
    }

    private fun updateRunning() {
        val running = isShown && started
        if (running != this.running) {
            if (running) {
                updateText(System.currentTimeMillis())
                postDelayed(mTickRunnable, 500)
            } else {
                removeCallbacks(mTickRunnable)
            }
            this.running = running
        }
    }

    private val mTickRunnable = object : Runnable {
        override fun run() {
            if (running) {
                updateText(System.currentTimeMillis())
                postDelayed(this, 1000)
            }
        }
    }
}
