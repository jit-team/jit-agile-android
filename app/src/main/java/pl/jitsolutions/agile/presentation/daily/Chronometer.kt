package pl.jitsolutions.agile.presentation.daily

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.widget.TextView
import pl.jitsolutions.agile.R

class Chronometer(context: Context, attributes: AttributeSet) : TextView(
    context,
    attributes
) {

    private var mBase = 0L
    private val timeElapsed = StringBuilder()
    private var mRunning = false
    private var mStarted = false

    init {
        text = context.getString(R.string.daily_screen_duration_label)
    }

    fun setBase(dailyStartInMillis: Long) {
        mBase = dailyStartInMillis + (15 * 60 * 1000)
    }

    fun start() {
        mStarted = true
        updateRunning()
    }

    fun stop() {
        mStarted = false
        updateRunning()
    }

    private fun updateText(now: Long) {
        val seconds = (mBase - now) / 1000
        text = DateUtils.formatElapsedTime(timeElapsed, seconds)
    }

    private fun updateRunning() {
        val running = isShown && mStarted
        if (running != mRunning) {
            if (running) {
                updateText(System.currentTimeMillis())
                postDelayed(mTickRunnable, 500)
            } else {
                removeCallbacks(mTickRunnable)
            }
            mRunning = running
        }
    }

    private val mTickRunnable = object : Runnable {
        override fun run() {
            if (mRunning) {
                updateText(System.currentTimeMillis())
                postDelayed(this, 1000)
            }
        }
    }
}
