package pl.jitsolutions.agile.presentation.daily

import android.content.Context
import pl.jitsolutions.agile.R
import java.util.Random

fun getQuote(context: Context): String {
    val stringArray = context.resources.getStringArray(R.array.daily_screen_quota)
    return stringArray.random()
}

private fun <E> Array<E>.random(): E = get(Random().nextInt(size))
