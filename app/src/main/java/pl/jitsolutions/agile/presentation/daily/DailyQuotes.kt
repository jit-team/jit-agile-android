package pl.jitsolutions.agile.presentation.daily

import android.content.Context
import pl.jitsolutions.agile.R
import java.util.Random

fun getQuote(context: Context): String {
    val list = ArrayList<String>()
    list.add(context.getString(R.string.daily_screen_quota1))
    list.add(context.getString(R.string.daily_screen_quota2))
    list.add(context.getString(R.string.daily_screen_quota3))
    list.add(context.getString(R.string.daily_screen_quota4))
    list.add(context.getString(R.string.daily_screen_quota5))
    list.add(context.getString(R.string.daily_screen_quota6))
    list.add(context.getString(R.string.daily_screen_quota7))
    return list.random()
}

private fun <E> List<E>.random(): E = get(Random().nextInt(size))
