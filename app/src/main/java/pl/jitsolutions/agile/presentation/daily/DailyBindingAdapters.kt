package pl.jitsolutions.agile.presentation.daily

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.User

@BindingAdapter(
    value = ["bindDailyUserList", "bindDailyUserListAdapter", "bindDailyUserListState"],
    requireAll = true
)
fun bindDailyWaitingUserList(
    recyclerView: RecyclerView,
    users: List<User>?,
    adapter: DailyListAdapter?,
    dailyState: DailyViewModel.DailyState
) {
    adapter?.let {
        recyclerView.adapter = it
        adapter.submitList(users)
        when (dailyState) {
            DailyViewModel.DailyState.End -> recyclerView.visibility = View.GONE
            else -> recyclerView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter(value = ["bindDailyEnd", "bindDailyEndVisibility"], requireAll = true)
fun bindDailyEnd(
    button: AppCompatButton,
    viewModel: DailyViewModel,
    dailyState: DailyViewModel.DailyState
) {
    val buttonVisibility = when (dailyState) {
        DailyViewModel.DailyState.End,
        DailyViewModel.DailyState.LastTurn,
        DailyViewModel.DailyState.LastWait -> View.GONE
        else -> View.VISIBLE
    }
    val buttonText = button.context.getString(R.string.daily_screen_end_button_text)

    with(button) {
        visibility = buttonVisibility
        text = buttonText
        setOnClickListener {
            showDailyEndConfirmation(button) { viewModel.endDaily() }
        }
    }
}

@BindingAdapter("bindDailyCountDownTimer", "bindDailyStartTime")
fun bindDailyCountDownTimer(
    chronometer: Chronometer,
    dailyState: DailyViewModel.DailyState,
    startTime: Long
) {
    if (dailyState is DailyViewModel.DailyState.Prepare || startTime == 0L) {
        return
    }
    if (startTime == -1L) {
        chronometer.stop()
        return
    }

    chronometer.setBase(startTime)
    chronometer.start()
}

@BindingAdapter("bindDailyNextTurnButton")
fun bindDailyNextTurnButton(button: Button, dailyState: DailyViewModel.DailyState) {
    val buttonVisibility = when (dailyState) {
        DailyViewModel.DailyState.End -> View.GONE
        else -> View.VISIBLE
    }
    val buttonText = when (dailyState) {
        DailyViewModel.DailyState.Prepare -> button.context.getString(R.string.daily_screen_start_button_text)
        DailyViewModel.DailyState.Wait -> button.context.getString(R.string.daily_screen_skip_button_text)
        DailyViewModel.DailyState.Turn -> button.context.getString(R.string.daily_screen_next_button_text)
        DailyViewModel.DailyState.LastWait,
        DailyViewModel.DailyState.LastTurn -> button.context.getString(R.string.daily_screen_end_daily_button_text)
        DailyViewModel.DailyState.End -> null
    }

    with(button) {
        visibility = buttonVisibility
        text = buttonText
    }
}

@BindingAdapter("bindDailyBackgroundColor")
fun bindBackgroundColor(
    coordinatorLayout: CoordinatorLayout,
    dailyState: DailyViewModel.DailyState
) {
    when (dailyState) {
        DailyViewModel.DailyState.Prepare -> {
            val colorFrom = (coordinatorLayout.background as ColorDrawable).color
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_prepare)
            transform(coordinatorLayout, colorFrom, colorTo)
        }
        DailyViewModel.DailyState.Wait, DailyViewModel.DailyState.LastWait -> {
            val colorFrom = (coordinatorLayout.background as ColorDrawable).color
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_wait)
            transform(coordinatorLayout, colorFrom, colorTo)
        }
        DailyViewModel.DailyState.Turn, DailyViewModel.DailyState.LastTurn -> {
            val colorFrom = (coordinatorLayout.background as ColorDrawable).color
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_turn)
            transform(coordinatorLayout, colorFrom, colorTo)
        }
        DailyViewModel.DailyState.End -> {
            val colorFrom = (coordinatorLayout.background as ColorDrawable).color
            val colorTo = ContextCompat.getColor(coordinatorLayout.context, R.color.daily_end)
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
    val textColor = when {
        user.current -> R.color.daily_current_user_text
        user.active -> R.color.daily_active_user_text
        else -> R.color.daily_other_user
    }
    val backgroundColor = when {
        user.current -> R.color.daily_current_user_background
        user.active -> R.color.daily_active_user_background
        else -> R.color.daily_other_user_background
    }

    with(textView) {
        text = if (user.name.isBlank()) user.email else user.name
        setTextColor(ContextCompat.getColor(textView.context, textColor))
        setBackgroundColor(ContextCompat.getColor(textView.context, backgroundColor))
    }
}

@BindingAdapter("bindDailyProgressVisibility")
fun bindDailyProgressVisibility(view: View, state: DailyViewModel.State) {
    view.visibility = when (state) {
        DailyViewModel.State.Idle -> View.INVISIBLE
        DailyViewModel.State.InProgress -> View.VISIBLE
    }
}

@BindingAdapter(value = ["bindDailyLeave", "bindDailyLeaveVisibility"], requireAll = false)
fun bindDailyLeave(view: View, onLeaveListener: () -> Unit, dailyState: DailyViewModel.DailyState) {
    val visibility = when (dailyState) {
        DailyViewModel.DailyState.End -> View.INVISIBLE
        else -> View.VISIBLE
    }
    view.visibility = visibility
    view.setOnClickListener {
        showDailyLeaveConfirmation(view, onLeaveListener)
    }
}

@BindingAdapter("bindDailyQuitVisibility")
fun bindDailyQuitVisibility(view: View, dailyState: DailyViewModel.DailyState) {
    val visibility = when (dailyState) {
        DailyViewModel.DailyState.End -> View.VISIBLE
        else -> View.GONE
    }
    view.visibility = visibility
}

@BindingAdapter("bindDailyQuotaState")
fun bindDailyQuotaState(textView: TextView, dailyState: DailyViewModel.DailyState) {
    when (dailyState) {
        DailyViewModel.DailyState.End -> with(textView) {
            text = getQuote(context)
            visibility = View.VISIBLE
        }
        else -> textView.visibility = View.GONE
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

private fun showDailyEndConfirmation(
    view: View,
    onEndListener: () -> Unit
) {
    AlertDialog.Builder(view.context)
        .setTitle(R.string.daily_screen_end_confirmation_title)
        .setMessage(R.string.daily_screen_end_confirmation_message)
        .setPositiveButton(R.string.daily_screen_end_confirmation_positive_button_text) { _, _ ->
            onEndListener.invoke()
        }
        .setNegativeButton(
            R.string.daily_screen_end_confirmation_negative_button_text,
            null
        )
        .setCancelable(true)
        .show()
}