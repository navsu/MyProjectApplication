package com.memandis.appbooking.binding

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

class SubscriptionAdapter<T, V>( val viewModel: V, @LayoutRes val mLayout: Int,
                                 diffCallBack: SubscriptionComparator<T>,
                                 private val subscriptionListener: SubscriptionListener<T>
) : ListAdapter<T, SubscriptionAdapter.SubscriptionViewHolder<T>>(diffCallBack)
 {

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder<T>{
         val layoutInflater = LayoutInflater.from(parent.context)
         val binding: ViewDataBinding = DataBindingUtil.inflate( layoutInflater, mLayout, parent,false )
         return SubscriptionViewHolder(binding)
     }

//     private fun onItemClick(any: Any) {
//
//     }

     override fun onBindViewHolder(holder: SubscriptionViewHolder<T>, position: Int) {
         holder.bind(data.getOrNull(position), viewModel, subscriptionListener)
     }

     var data: List<T> = emptyList()
         set(v) {
             field = v
             notifyDataSetChanged()
         }

    class SubscriptionViewHolder<T>(private val binding: ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        fun <V> bind(item: T?, viewModel: V, baseListener: SubscriptionListener<T> ) {
//           binding.setVariable(BR.item, item)
//           binding.setVariable(BR.viewModel, viewModel)
//           binding.setVariable(BR.clickListener, baseListener)
//            binding.setVariable(BR.position, adapterPosition)
//           binding.setVariable(BR.view, fragment)
           binding.executePendingBindings()
        }

    }

     override fun getItemCount(): Int { return data.size }

}

open class SubscriptionListener<T>(val clickListener: (item: T, position: Int) -> Unit) {
    fun onClick(item: T, position: Int) = clickListener(item, position)
}

class SubscriptionComparator<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem == newItem)
    }

}

@BindingAdapter("subscriptionData")
fun <T> setSubscriptionRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    if (data == null) {
        return
    }

    if (recyclerView.adapter is SubscriptionAdapter<*,*>) {
        (recyclerView.adapter as SubscriptionAdapter<T,*>).data = data
    }
}
