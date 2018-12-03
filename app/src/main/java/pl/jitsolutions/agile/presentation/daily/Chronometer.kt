package pl.jitsolutions.agile.presentation.daily

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import pl.jitsolutions.agile.R

private const val DAILY_LENGTH = 10 * 1000L
private const val INVALID_TIME = -1L

class Chronometer(context: Context, attributes: AttributeSet) : TextView(
    context,
    attributes
) {

    private var based = INVALID_TIME
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
        setTextColor(ContextCompat.getColor(context, R.color.daily_countdown_timer_default_text))
    }

    fun stop() {
        started = false
        updateRunning()
        if (based == INVALID_TIME) {
            text = DateUtils.formatElapsedTime(timeElapsed, 0)
            return
        }
        updateText(System.currentTimeMillis())
    }

    private fun updateText(now: Long) {
        val seconds = (based - now) / 1000
        if (seconds > 0) {
            positive(seconds)
        } else {
            negative(seconds)
        }
    }

    private fun positive(seconds: Long) {
        val time = DateUtils.formatElapsedTime(timeElapsed, seconds)
        text = time
    }

    private fun negative(seconds: Long) {
        val minus = context.getString(R.string.daily_screen_duration_minus_char)
        val time = minus.plus(DateUtils.formatElapsedTime(timeElapsed, -seconds))
        text = time
        setTextColor(ContextCompat.getColor(context, R.color.daily_countdown_timer_negative_text))
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
