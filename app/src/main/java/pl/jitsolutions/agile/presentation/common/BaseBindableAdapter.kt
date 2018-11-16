package pl.jitsolutions.agile.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.BR

abstract class BaseBindableAdapter<T>(
    val clickListener: (T) -> Unit,
    diffCallback: DiffUtil.ItemCallback<T>
) :
    ListAdapter<T, DataBindingViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.setVariable(BR.listItem, item)
        holder.binding.setVariable(BR.clickListener, object :
            OnItemClickListener<T> {
            override fun onItemClick(item: T) = clickListener.invoke(item)
        })
        setupBinding(holder.binding, item as T)
        holder.binding.executePendingBindings()
    }

    open fun setupBinding(binding: ViewDataBinding, item: T) = Unit

    interface OnItemClickListener<T> {
        fun onItemClick(item: T)
    }
}

class DataBindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)