package pl.jitsolutions.agile.presentation.daily

import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.presentation.common.BaseBindableAdapter
import pl.jitsolutions.agile.presentation.common.UserDiffCallback

class DailyListAdapter : BaseBindableAdapter<User>({}, UserDiffCallback()) {

    override fun getItemViewType(position: Int) = R.layout.list_item_daily_user
}