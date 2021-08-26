package com.memandis.appbooking.scheduling

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.memandis.appbooking.BR

class SchedulingAdapter<T, V>( val viewModel: V, @LayoutRes val mLayout: Int,
                                 diffCallBack: SchedulingComparator<T>,
                                 private val SchedulingListener: SchedulingListener<T>)
    : ListAdapter<T, SchedulingAdapter.SchedulingViewHolder<T>>(diffCallBack)
 {

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulingViewHolder<T>{
         val layoutInflater = LayoutInflater.from(parent.context)
         val binding: ViewDataBinding = DataBindingUtil.inflate(
             layoutInflater, mLayout, parent,false )
         return SchedulingViewHolder(binding)
     }

     override fun onBindViewHolder(holder: SchedulingViewHolder<T>, position: Int) {
         holder.bind(data.getOrNull(position), viewModel, SchedulingListener)
     }

     var data: List<T> = emptyList()
         set(v) {
             field = v
             notifyDataSetChanged()
         }

    class SchedulingViewHolder<T>(private val binding: ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        fun <V> bind(item: T?, viewModel: V, baseListener: SchedulingListener<T> ) {
           binding.setVariable(BR.item, item)
//           binding.setVariable(BR.viewModel, viewModel)
           binding.setVariable(BR.clickListener, baseListener)
           binding.setVariable(BR.position, adapterPosition)
           binding.executePendingBindings()
        }

    }

     override fun getItemCount(): Int { return data.size }

}

open class SchedulingListener<T>(val clickListener: (item: T, position: Int) -> Unit) {
    fun onClick(item: T, position: Int) = clickListener(item, position)
}

class SchedulingComparator<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem == newItem)
    }

}

@BindingAdapter("schedulingData")
fun <T> setSchedulingRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    if (data == null) {
        return
    }

    if (recyclerView.adapter is SchedulingAdapter<*,*>) {
        (recyclerView.adapter as SchedulingAdapter<T,*>).data = data
    }
}
