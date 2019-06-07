package pl.jitsolutions.agile.presentation.planningpoker

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