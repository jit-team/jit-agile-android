package pl.jitsolutions.agile.presentation.daily

import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Group
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
        DailyViewModel.DailyState.Prepare,
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

@BindingAdapter("bindDailyUser")
fun bindDailyUser(textView: TextView, user: User) {
    with(textView) {
        text = if (user.name.isBlank()) user.email else user.name
    }
}

@BindingAdapter("bindDailyProgressVisibility")
fun bindDailyProgressVisibility(view: View, state: DailyViewModel.State) {
    view.visibility = when (state) {
        DailyViewModel.State.Idle -> View.GONE
        DailyViewModel.State.InProgress -> View.VISIBLE
        DailyViewModel.State.Success -> View.GONE
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

@BindingAdapter("bindDailyViewEnabled")
fun bindDailyViewEnabled(view: View, state: DailyViewModel.State) {
    view.isEnabled = state == DailyViewModel.State.Success
}

@BindingAdapter("bindDailyPlaySound")
fun bindDailySound(view: View, playSound: Boolean) {
    if (playSound) {
        val mp = MediaPlayer.create(view.context, R.raw.start_daily_sound)
        mp.start()
    }
}

@BindingAdapter("bindDailyUserBackground")
fun bindDailyUserBackground(layout: CardView, user: User) {
    val backgroundColor = when {
        user.current -> ContextCompat.getColor(layout.context, R.color.daily_active_user_background)
        else -> Color.TRANSPARENT
    }
    val elevation = when {
        user.current -> layout.context.resources.getDimension(R.dimen.daily_item_user_elevation)
        else -> 0f
    }

    with(layout) {
        setCardBackgroundColor(backgroundColor)
        this.elevation = elevation
    }
}

@BindingAdapter("bindDailyUserStatus")
fun bindDailyUserStatus(view: View, user: User) {
    val color = when {
        user.current -> R.color.daily_status_dot_current_user_color
        user.active -> R.color.daily_status_dot_active_user_color
        else -> R.color.daily_status_dot_inactive_user_color
    }
    view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, color))
}

@BindingAdapter("bindDailyProjectInfoBackground")
fun bindDailyProjectInfoBackground(view: View, state: DailyViewModel.DailyState) {
    val backgroundColor = when (state) {
        DailyViewModel.DailyState.Prepare -> R.color.daily_card_inactive_color
        DailyViewModel.DailyState.End -> R.color.daily_card_inactive_color
        else -> R.color.daily_card_active_color
    }
    view.setBackgroundColor(ContextCompat.getColor(view.context, backgroundColor))
}

@BindingAdapter("bindDailyUserAlpha")
fun bindDailyUserAlpha(view: View, user: User) {
    val alpha = when {
        !user.active -> 0.8f
        else -> 1f
    }
    view.alpha = alpha
}

@BindingAdapter("bindDailyStatus")
fun bindDailyStatus(textView: TextView, state: DailyViewModel.DailyState) {
    val textId = when (state) {
        DailyViewModel.DailyState.Prepare -> R.string.daily_screen_status_preparing
        DailyViewModel.DailyState.End -> R.string.daily_screen_status_end
        else -> R.string.daily_screen_status_running
    }
    textView.text = textView.context.getString(textId)
}

@BindingAdapter("bindDailyGroupVisibility")
fun bindDailyGroupVisibility(group: Group, state: DailyViewModel.State) {
    when (state) {
        DailyViewModel.State.Success -> group.visibility = View.VISIBLE
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