package pl.jitsolutions.agile.presentation.common

import androidx.recyclerview.widget.DiffUtil
import pl.jitsolutions.agile.domain.User

class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return true
    }
}