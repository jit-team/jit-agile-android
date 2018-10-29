package pl.jitsolutions.agile.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.jitsolutions.agile.BR

abstract class BaseBindableAdapter : RecyclerView.Adapter<BaseBindableAdapter.BindableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BindableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val obj = getObjForPosition(position)
        holder.bind(obj)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    protected abstract fun getObjForPosition(position: Int): Any

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun itemClicked(position: Int)

    inner class BindableViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any) {
            binding.setVariable(BR.listItem, obj)
            binding.setVariable(BR.clickHandler, clickHandler)
            binding.executePendingBindings()
        }

        private val clickHandler = object : RecyclerViewClickHandler {
            override fun click() {
                itemClicked(adapterPosition)
            }
        }
    }
}