package com.memandis.appbooking.binding

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.memandis.appbooking.databinding.ItemProviderBookingBasicBinding
import com.memandis.appbooking.databinding.ItemProviderBookingPremiumBinding
import com.memandis.appbooking.databinding.ItemProviderBookingStandardBinding
import com.memandis.appbooking.vm.BookingViewModel

class BookingSlotAdapter<T, V>(
    val viewModel: V,
    diffCallBack: BookingComparator<T>,
    private val bookingListener: BookingListener<T>,
    private val bookingSlotListener: BookingSlotListener<T>
) : ListAdapter<T, RecyclerView.ViewHolder>(diffCallBack)
 {

     private val BASIC = 0
     private val STANDARD = 1
     private val PREMIUM = 2

     override fun getItemViewType(position: Int): Int {
         return if (position == 0) {
             BASIC
         } else return if (position == 1) {
             STANDARD
         } else {
             PREMIUM
         }
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         val inflater = LayoutInflater.from(parent.context)
//         val objectType = when (directoryType) {
//             UnixFileType.D -> "d"
//             UnixFileType.HYPHEN_MINUS -> "-"
//             UnixFileType.L -> "l"
//         }
//         when (viewType) {
//             BASIC -> return  DesignerBasicBookingViewHolder<T>(
//                 ItemProviderBookingBasicBinding.inflate(inflater, parent, false)
//                 /*, viewType, bookingListener*/)
//
//             STANDARD -> return DesignerStandardBookingViewHolder<T>(
//                 ItemProviderBookingStandardBinding.inflate(inflater, parent, false)
//                 /*, viewType, bookingListener*/)
//
//             PREMIUM -> return DesignerPremiumBookingViewHolder<T>(
//                 ItemProviderBookingPremiumBinding.inflate(inflater, parent, false)
//                 /*, viewType, bookingListener*/)
//         }

         if (viewType == BASIC) {
             val binding = ItemProviderBookingBasicBinding.inflate(inflater, parent, false)
             return  DesignerBasicBookingViewHolder<T>(binding /*, viewType, bookingListener*/)
         } else if (viewType == STANDARD) {
             val binding = ItemProviderBookingStandardBinding.inflate(inflater, parent, false)
            return DesignerStandardBookingViewHolder<T>(binding/*, viewType, bookingListener*/)
         } else {
             val binding = ItemProviderBookingPremiumBinding.inflate(inflater, parent, false)
             return DesignerPremiumBookingViewHolder<T>(binding/*, viewType, bookingListener*/)
         }
     }

     override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

         val itemType = getItemViewType(position)
         if (itemType == BASIC) {
             (holder as DesignerBasicBookingViewHolder<T>).bind(
                 data.getOrNull(position), viewModel, bookingListener, bookingSlotListener
             )
         } else if (itemType == STANDARD) {
             (holder as DesignerStandardBookingViewHolder<T>).bind(
                 data.getOrNull(position), viewModel, bookingListener, bookingSlotListener
             )
         } else {
             (holder as DesignerPremiumBookingViewHolder<T>).bind(
                 data.getOrNull(position), viewModel, bookingListener, bookingSlotListener
             )
         }

     }

     var data: List<T> = emptyList()
         set(v) {
             field = v
             notifyDataSetChanged()
         }

     class DesignerBasicBookingViewHolder<T>(
         private val binding: ViewDataBinding
     ) : RecyclerView.ViewHolder(binding.root) {

         fun <V> bind(
             item: T?,
             viewModel: V,
             bookingListener: BookingListener<T>,
             bookingSlotListener: BookingSlotListener<T>
         ) {
//             binding.setVariable(BR.viewModel, viewModel)
//             binding.setVariable(BR.basicSlots, item)
//             binding.setVariable(BR.basicBookingClickListener, bookingListener)
//             binding.setVariable(BR.basicBookingSlotClickListener, bookingSlotListener)
//             binding.setVariable(BR.basicPosition, 0)
             binding.executePendingBindings()
         }

     }

     class DesignerStandardBookingViewHolder<T>(private val binding: ViewDataBinding) :
         RecyclerView.ViewHolder(binding.root) {

         fun <V> bind(
             item: T?,
             viewModel: V,
             bookingListener: BookingListener<T>,
             bookingSlotListener: BookingSlotListener<T>
         ) {
//             binding.setVariable(BR.viewModel, viewModel)
//             binding.setVariable(BR.standardSlots, item)
//             binding.setVariable(BR.standardPosition, 0)
//             binding.setVariable(BR.standardBookingClickListener, bookingListener)
//             binding.setVariable(BR.standardBookingSlotClickListener, bookingSlotListener)
             binding.executePendingBindings()
         }

    }

    class DesignerPremiumBookingViewHolder<T>(private val binding: ViewDataBinding) :
         RecyclerView.ViewHolder(binding.root) {

         fun <V> bind(
             item: T?,
             viewModel: V,
             bookingListener: BookingListener<T>,
             bookingSlotListener: BookingSlotListener<T>
         ) {
//             binding.setVariable(BR.viewModel, viewModel)
//             binding.setVariable(BR.premiumSlots, item)
//             binding.setVariable(BR.premiumPosition, 0)
//             binding.setVariable(BR.premiumBookingClickListener, bookingListener)
//             binding.setVariable(BR.premiumBookingSlotClickListener, bookingSlotListener)

             binding.executePendingBindings()
         }

     }

     override fun getItemCount(): Int { return data.size }

 }

class BookingSlotListener<I>(val clickListener: (item: I, currPosition: Int, value: String) -> Unit) {
     fun onClickItem(item: I, currPosition: Int, value: String) = clickListener(
         item,
         currPosition,
         value
     )
}

open class BookingListener<T>(val clickListener: (item: T, position: Int) -> Unit) {
    fun onClick(item: T, position: Int) = clickListener(item, position)
}

class BookingComparator<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return (oldItem == newItem)
    }

}

@BindingAdapter("bookingData")
fun <T> setViewPager2Properties(pager: ViewPager2, data: List<T>?)
{
    if (data == null) {
        return
    }
    if (pager.adapter is BookingSlotAdapter<*, *>) {
        (pager.adapter as BookingSlotAdapter<T, *>).data = data
    }
}


@BindingAdapter("setBookingTapContents", "setBookingVm", requireAll = false)
fun setBookingTapContents( tabLayout: TabLayout, items: List<String>?,  mainVm: BookingViewModel?)
{
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
                        mainVm?.selectTab(position)
                    }
                }
            })
        }
    }
}

@BindingAdapter("setBookingViewPosition")
fun setBookingViewPosition(view: View, position: Int?)
{
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

@BindingAdapter("setSelectedSlotImage", "slotPressed", "slotNormal")
fun setSelectedSlotImage(view: ImageView, value: Boolean, pressed: Drawable?, normal: Drawable?) {
    if(value) {
        view.setImageDrawable(pressed)
    } else {
        view.setImageDrawable(normal)
    }
}