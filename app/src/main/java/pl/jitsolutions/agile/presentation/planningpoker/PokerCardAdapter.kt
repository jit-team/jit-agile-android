package pl.jitsolutions.agile.presentation.planningpoker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.databinding.ListItemPokerCardBinding
import pl.jitsolutions.agile.domain.getCards

class PokerCardAdapter : PagerAdapter() {

    val items = getCards()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding: ListItemPokerCardBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item_poker_card, container, false)
        binding.listItem = items[position]
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(`object` as View)

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount(): Int = items.size
}