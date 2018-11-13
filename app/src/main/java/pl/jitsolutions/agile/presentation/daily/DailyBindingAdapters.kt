package pl.jitsolutions.agile.presentation.daily

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.User

@BindingAdapter(
    value = ["bindDailyUserList", "bindDailyUserListAdapter"],
    requireAll = true
)
fun bindDailyWaitingUserList(
    recyclerView: RecyclerView,
    users: List<User>?,
    adapter: DailyListAdapter?
) {
    adapter?.let {
        recyclerView.adapter = it
        it.users = users ?: emptyList()
    }
}

@BindingAdapter("bindDailyFirstButton")
fun bindFirstButton(button: Button, dailyState: DailyViewModel.DailyState) {
    when (dailyState) {
        DailyViewModel.DailyState.Prepare -> {
            with(button) {
                text = button.context.getString(R.string.daily_screen_end_button_text)
            }
        }
        DailyViewModel.DailyState.Wait -> {
            with(button) {
                text = button.context.getString(R.string.daily_screen_end_button_text)
            }
        }
        DailyViewModel.DailyState.Turn -> {
            with(button) {
                text = button.context.getString(R.string.daily_screen_end_button_text)
            }
        }
    }
}

@BindingAdapter("bindDailyCountDownTimer")
fun bindCountDownTimer(chronometer: Chronometer, dailyState: DailyViewModel.DailyState) {
    if (dailyState is DailyViewModel.DailyState.Prepare) {
        return
    }
    chronometer.setBase(System.currentTimeMillis())
    chronometer.start()
}

@BindingAdapter("bindDailySecondButton")
fun bindSecondButton(button: Button, dailyState: DailyViewModel.DailyState) {
    when (dailyState) {
        DailyViewModel.DailyState.Prepare -> {
            with(button) {
                text = button.context.getString(R.string.daily_screen_start_button_text)
            }
        }
        DailyViewModel.DailyState.Wait -> {
            with(button) {
                text = button.context.getString(R.string.daily_screen_skip_button_text)
            }
        }
        DailyViewModel.DailyState.Turn -> {
            with(button) {
                text = button.context.getString(R.string.daily_screen_next_button_text)
            }
        }
    }
}

@BindingAdapter("bindDailyBackgroundColor")
fun bindBackgroundColor(
    coordinatorLayout: CoordinatorLayout,
    dailyState: DailyViewModel.DailyState
) {
    when (dailyState) {
        DailyViewModel.DailyState.Prepare -> {
            val colorFrom = Color.WHITE
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_prepare)
            transform(coordinatorLayout, colorFrom, colorTo)
        }
        DailyViewModel.DailyState.Wait -> {
            val colorFrom = (coordinatorLayout.background as ColorDrawable).color
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_wait)
            transform(coordinatorLayout, colorFrom, colorTo)
        }
        DailyViewModel.DailyState.Turn -> {
            val colorFrom = (coordinatorLayout.background as ColorDrawable).color
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_turn)
            transform(coordinatorLayout, colorFrom, colorTo)
        }
    }
}

private fun transform(coordinatorLayout: CoordinatorLayout, fromColor: Int, toColor: Int) {
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
    colorAnimation.duration = 800
    colorAnimation.addUpdateListener { animator ->
        coordinatorLayout.setBackgroundColor(
            animator.animatedValue as Int
        )
    }
    colorAnimation.start()
}

@BindingAdapter("bindDailyUser")
fun bindDailyUser(textView: TextView, user: User) {
    val name = if (user.name.isBlank()) user.email else user.name
    if (user.active) {
        textView.text = SpannableStringBuilder().append(
            name,
            StyleSpan(Typeface.BOLD),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    } else {
        textView.text = name
    }
}

@BindingAdapter("bindDailyProgressVisibility")
fun bindDailyProgressVisibility(view: View, state: DailyViewModel.State) {
    view.visibility = when (state) {
        DailyViewModel.State.Idle -> View.INVISIBLE
        DailyViewModel.State.InProgress -> View.VISIBLE
    }
}

@BindingAdapter("bindDailyLeave")
fun bindDailyLeave(view: View, onLeaveListener: () -> Unit) {
    view.setOnClickListener {
        showDailyLeaveConfirmation(view, onLeaveListener)
    }
}

fun showDailyLeaveConfirmation(
    view: View,
    onLeaveListener: () -> Unit
) {
    AlertDialog.Builder(view.context)
        .setTitle(R.string.daily_screen_leave_confirmation_title)
        .setMessage(R.string.daily_screen_leave_confirmation_message)
        .setPositiveButton(R.string.daily_screen_leave_confirmation_positive_button_text) { _, _ ->
            onLeaveListener.invoke()
        }
        .setNegativeButton(
            R.string.daily_screen_leave_confirmation_negative_button_text,
            null
        )
        .setCancelable(true)
        .show()
}