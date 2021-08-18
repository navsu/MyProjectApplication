package com.memandis.appbooking.binding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.memandis.appbooking.vm.BookingViewModel

class ProviderProfileAdapter<T, V>(
    val viewModel: V, @LayoutRes val mLayout: Int,
    diffCallBack: ProfileComparator<T>,
    private val providerListener: ProfileListener<T>
)
    : ListAdapter<T, ProviderProfileAdapter.ProfileViewHolder<T>>(diffCallBack)
 {

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder<T>{
         val layoutInflater = LayoutInflater.from(parent.context)
         val binding: ViewDataBinding = DataBindingUtil.inflate(
             layoutInflater, mLayout,
             parent, false
         )
         return ProfileViewHolder(binding/*, fragment*/)
     }

     override fun onBindViewHolder(holder: ProfileViewHolder<T>, position: Int) {
         holder.bind(data.getOrNull(position), viewModel, providerListener)
     }

     var data: List<T> = emptyList()
         set(v) {
             field = v
             notifyDataSetChanged()
         }

    class ProfileViewHolder<T>(private val binding: ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        fun <V> bind(item: T?, viewModel: V, baseListener: ProfileListener<T>) {
//           binding.setVariable(BR.item, item)
//           binding.setVariable(BR.viewModel, viewModel)
//           binding.setVariable(BR.clickListener, baseListener)
//            binding.setVariable(BR.key, adapterPosition)
//           binding.setVariable(BR.view, fragment)
           binding.executePendingBindings()
        }
    }

     override fun getItemCount(): Int { return data.size }

 }

open class ProfileListener<T>(val clickListener: (item: T, position: Int) -> Unit) {
    fun onClick(item: T, position: Int) = clickListener(item, position)
}

class ProfileComparator<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem == newItem)
    }

}

@BindingAdapter("pagerData")
fun <T> setAdapter(pager: ViewPager2, data: List<T>?) {
    if (data == null) {
        return
    }

    if (pager.adapter is ProviderProfileAdapter<*, *>) {
        (pager.adapter as ProviderProfileAdapter<T, *>).data = data
    }
}

@BindingAdapter("setVm", "profileData")
fun <T, V> setViewPager2Properties(viewPager: ViewPager2, mainVm: V, data: List<T>?) {
    if (!data.isNullOrEmpty())
        viewPager.adapter?.run {
            if (this is ProviderProfileAdapter<*, *>) {
                (this as ProviderProfileAdapter<T, *>).data = data
            }
        } ?: kotlin.run {

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    //Nothing.
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    //Nothing.
                }

                override fun onPageSelected(position: Int) {
                    (mainVm as BookingViewModel).selectPosition(position)
                }
            })
        }
}

@BindingAdapter("setTapContents", "setVm")
fun setTapContents(tabLayout: TabLayout, items: List<String>?, mainVm: BookingViewModel?) {
    items?.forEach {
        with(tabLayout) {
            newTab().let { tab ->
                tab.text = it
                addTab(tab)
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //Nothing.
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //Nothing.
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { position ->
                        mainVm?.selectPosition(position)
                    }
                }
            })
        }
    }
}

@BindingAdapter("setViewPosition")
fun setViewPosition(view: View, position: Int?) {
    if (position != null)
        when (view) {
            is ViewPager2 -> {
                view.currentItem = position
            }
            is TabLayout -> {
                view.run {
                    getTabAt(position)?.let { tab ->
                        selectTab(tab)
                    }
                }
            }
        }
}

//@BindingAdapter("setUpWithViewpager")
//fun setUpWithViewpager(tabLayout: TabLayout, viewPager: ViewPager2) {
//    tabLayout.setupWithViewPager(viewPager)
//}