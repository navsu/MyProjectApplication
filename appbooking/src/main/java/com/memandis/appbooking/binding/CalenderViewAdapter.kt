package com.memandis.appbooking.binding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class CalenderViewAdapter<T, V>(val viewModel: V, @LayoutRes val mLayout: Int,
                                diffCallBack: CalendarComparator<T>,
                                private val calenderListener: CalendarListener<T>
)
    : ListAdapter<T, CalenderViewAdapter.CalendarViewHolder<T>>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder<T> {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: ViewDataBinding = DataBindingUtil.inflate(
                layoutInflater,
                mLayout,
                parent,
                false
            )
            return CalendarViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CalendarViewHolder<T>, position: Int) {
            holder.bind(data.getOrNull(position), viewModel, calenderListener)
        }

        var data: List<T> = emptyList()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

        class CalendarViewHolder<T>(private val binding: ViewDataBinding)
            : RecyclerView.ViewHolder(binding.root)
        {

            fun <V> bind(item: T?, viewModel: V, baseListener: CalendarListener<T>) {

//                binding.setVariable(BR.item, item)
//                binding.setVariable(BR.viewModel, viewModel)
//                binding.setVariable(BR.position, adapterPosition)
//                binding.setVariable(BR.prevPosition, oldPosition)
//                binding.setVariable(BR.calenderClickListener, baseListener)
                binding.executePendingBindings()
            }
        }

        override fun getItemCount(): Int { return data.size }

    }

     class CalendarListener<T>(val clickListener: (item: T, position: Int) -> Unit) {
        fun onClick(item: T, position: Int) =clickListener(item, position)
    }

    class CalendarComparator<T> : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem === newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return (oldItem == newItem)
        }

    }

@BindingAdapter("getCalenderData")
fun <T> setBookingCalenderData(recyclerView: RecyclerView, data: List<T>?) {
    if (data == null) {
        return
    }

    if (recyclerView.adapter is CalenderViewAdapter<*, *>) {
        (recyclerView.adapter as CalenderViewAdapter<T, *>).data = data
    }
}

@BindingAdapter("setSelectedDateBackground", "datePressed", "dateNormal")
fun setSelectedDateBackground(view: CardView, value: Boolean, pressed: Int, normal: Int) {
    if(value) {
        view.setCardBackgroundColor(pressed)
    } else {
        view.setCardBackgroundColor(normal)
    }

}