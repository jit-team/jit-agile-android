package pl.jitsolutions.agile.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.BR

abstract class BaseBindableAdapter<T>(val clickListener: (T) -> Unit) :
    RecyclerView.Adapter<BaseBindableAdapter<T>.BindableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BindableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val obj = getObjForPosition(position)
        holder.binding.setVariable(BR.listItem, obj)
        holder.binding.setVariable(BR.clickListener, object : OnItemClickListener<T> {
            override fun onItemClick(item: T) = clickListener.invoke(item)
        })
        holder.binding.executePendingBindings()
    }

    open fun setupBinding(binding: ViewDataBinding) = Unit

    override fun getItemViewType(position: Int) = getLayoutIdForPosition(position)

    protected abstract fun getObjForPosition(position: Int): Any

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    inner class BindableViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener<T> {
        fun onItemClick(item: T)
    }
}