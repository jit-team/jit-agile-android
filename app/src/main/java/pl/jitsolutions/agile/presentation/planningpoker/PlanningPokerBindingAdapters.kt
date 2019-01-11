package pl.jitsolutions.agile.presentation.planningpoker

import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import pl.jitsolutions.agile.R

@BindingAdapter("bindPlanningPokerAdapter")
fun bindPlanningPokerAdapter(
    viewPager: ViewPager,
    adapter: PokerCardAdapter
) {
    viewPager.adapter = adapter
    viewPager.setZoomAnimation()
}

private fun ViewPager.setZoomAnimation() {
    clipToPadding = false
    val vertical =
        context.resources.getDimensionPixelSize(R.dimen.planning_poker_card_padding_vertical)
    val horizontal =
        context.resources.getDimensionPixelSize(R.dimen.planning_poker_card_padding_horizontal)
    setPadding(horizontal, vertical, horizontal, vertical)

    setPageTransformer(false) { view, position ->
        val scalePercent = 0.9
        val scaleFactor = scalePercent + (1 - scalePercent) * (1 - Math.abs(position))
        view.scaleX = scaleFactor.toFloat()
        view.scaleY = scaleFactor.toFloat()

        val scale = 1.1026785f - Math.abs(position)
        val scaleElevation = scale * 20f
        view.elevation = scaleElevation
    }
}

@BindingAdapter("bindPlanningPokerBackArrowVisibility")
fun bindPlanningPokerBackArrowVisibility(view: View, bind: Boolean) {
    if (bind) {
        val wrapper = view.context as? ContextWrapper
        val activity = wrapper?.baseContext as? AppCompatActivity
        activity?.apply {
            setSupportActionBar(view as? Toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}