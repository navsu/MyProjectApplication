package com.memandis.project.main
/*
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class ServicesViewAdapter<T, V>(val viewModel: V, @LayoutRes val mLayout: Int,
                                private val serviceListener:ServiceListener<T>,
                               val context: ServiceFragment,
                                diffCallBack: ServiceDataComparator<T>,
                                private val itemWidth: Int)
    : ListAdapter<T, ServicesViewAdapter.ServiceViewHolder<T>>(diffCallBack)
{


    var data: List<T> = emptyList()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            layoutInflater, mLayout, parent,
            false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder<T>, position: Int) {
        val clickListener = View.OnClickListener { view ->
            Log.d("Project_Adapter", "user projects images...$position")
//            val extras = FragmentNavigatorExtras(view.toTransitionGroup())
//            val destination = ProjectViewFragmentDirections.
//                             actionProjectViewFragmentToProjectPagerFragment(position)
////            view.findNavController().navigate(destination, extras)
////            val destination = HomeFragmentDirections.actionHomeToProjectView(it.projectId)
//            with(NavHostFragment.findNavController(context)) {
//                currentDestination?.getAction(destination.actionId)?.let {
//                    navigate(destination, extras)
//                }
//            }
        }
//        holder.bind(data.getOrNull(position), viewModel, itemWidth, projectListener)
        holder.bind(data.getOrNull(position), viewModel, itemWidth, clickListener)
    }

    override fun getItemCount() = data.size

    class ServiceViewHolder<T>(private val binding: ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun <V> bind(
            item: T?,
            viewModel: V,
            itemWidth: Int/*, projectListener:ProjectListener<T>*/,
            clickListener: View.OnClickListener
        ) {
            val adapterPosition = adapterPosition
//            binding.setVariable(BR.item, item)
//            binding.setVariable(BR.itemWidth, itemWidth)
//            binding.setVariable(BR.position, adapterPosition)
//            binding.setVariable(BR.viewModel, viewModel)
//            binding.setVariable(BR.clickListener,clickListener/* projectListener*/)

            // Set the string value of the image resource as the unique transition name for the view.
            binding.executePendingBindings()

        }

    }

}

class ServiceDataComparator<T>  : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem == newItem)
    }

}

open class ServiceListener<T>(val clickListener: (item: T, position: Int) -> Unit) {
    fun onClick(item: T, position: Int) = clickListener(item, position)
}

//@BindingAdapter("servivevariable_width")
//fun setProjectImageSize(view: View, width: Int) {
//    val layoutParams = FrameLayout.LayoutParams(view.layoutParams)
//    val ratio = 1024/768//current.height / obj.width
//    layoutParams.width = width
//    layoutParams.height = (width* ratio)
//    view.layoutParams = layoutParams
//}

@BindingAdapter("serviceImageData")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    if (data == null) {
        return
    }

    if (recyclerView.adapter is ServicesViewAdapter<*, *>) {
        (recyclerView.adapter as ServicesViewAdapter<T, *>).data = data
    }
}

//@BindingAdapter("android:visibility")
//fun setVisibility(view: View, value: String) {
//    view.visibility = if (value == "video") View.VISIBLE else View.GONE
//}
*/