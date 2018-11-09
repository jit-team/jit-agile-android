package pl.jitsolutions.agile.presentation.daily

import pl.jitsolutions.agile.R
import pl.jitsolutions.agile.domain.User
import pl.jitsolutions.agile.presentation.common.BaseBindableAdapter

class DailyListAdapter : BaseBindableAdapter<User>({}) {

    var users: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getObjForPosition(position: Int) = users[position]

    override fun getLayoutIdForPosition(position: Int) = R.layout.list_item_daily_user

    override fun getItemCount() = users.size
}