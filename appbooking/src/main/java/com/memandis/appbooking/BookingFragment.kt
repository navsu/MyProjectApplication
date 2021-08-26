package com.memandis.appbooking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.memandis.appbooking.binding.BookingComparator
import com.memandis.appbooking.binding.BookingListener
import com.memandis.appbooking.binding.BookingSlotAdapter
import com.memandis.appbooking.binding.BookingSlotListener
import com.memandis.appbooking.databinding.FragmentBookingBinding
import com.memandis.appbooking.scheduling.SchedulingAdapter
import com.memandis.appbooking.scheduling.SchedulingComparator
import com.memandis.appbooking.scheduling.SchedulingListener
import com.memandis.appbooking.scheduling.SchedulingViewModel
import com.memandis.appbooking.vm.BookingViewModel
import com.memandis.remote.datasource.model.booking.Slot
import com.memandis.remote.utils.bookingutils.getNextBusinessDay
import com.svayantra.core.models.Booking
//import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//@AndroidEntryPoint
open class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val args: BookingFragmentArgs by navArgs()

//    private val bookingViewModel by viewModels<BookingViewModel> {  getViewModelFactory() }

//    private val bookingViewModel: BookingViewModel by viewModels()
//    private val scheduleViewModel: SchedulingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val scheduleViewModel: SchedulingViewModel = ViewModelProvider(
            this.requireActivity()).get(SchedulingViewModel::class.java)

        binding.apply {

            recyclerViewAvailableTime.apply {

                adapter = SchedulingAdapter<Slot, SchedulingViewModel>(
                    scheduleViewModel,
                    R.layout.available_time_item,
                    SchedulingComparator<Slot>(),
                    SchedulingListener<Slot> { date: Slot, selected: Int ->

                    }
                )
                layoutManager = LinearLayoutManager(context)

            }

//        binding.mainViewCustomCalendar.setOnDateSelectedListener(this);
//
//            binding.bookingCalendarView.apply {
//
//                adapter = CalenderViewAdapter<CalendarEntity, BookingViewModel>(
//                    bookingViewModel,
//                    R.layout.item_provider_booking_calender,
//                    CalendarComparator<CalendarEntity>(),
//                    CalendarListener<CalendarEntity> { date: CalendarEntity, selected: Int ->
////                        Toast.makeText( context,
////                                "full date  ${date.fullDate}" +
////                                    " day ${date.day}" +
////                                    " date ${date.date}" +
////                                    " month ${date.month}" +
////                                    " year ${date.year}" +
////                                    " selected $selected",
////                                    Toast.LENGTH_SHORT ).show()
////                        val c = Calendar.getInstance()
////                        val day = c.get(Calendar.DAY_OF_MONTH)
////                        val index = selected - day
////                        bookingViewModel.setCalenderEntity(date)
//                        bookingViewModel.selectedDatePosition(selected)
//                        bookingViewModel.selectedSlotDateId(date.date.toInt())
//                        bookingViewModel.selectedSlotDate(date.fullDate)
//                        (binding.bookingCalendarView
//                            .adapter as CalenderViewAdapter<CalendarEntity, BookingViewModel>)
//                            .notifyDataSetChanged()
//                        (binding.designerBookingPager
//                            .adapter as BookingSlotAdapter<SlotData.kt, BookingViewModel>)
//                            .notifyDataSetChanged()
//                    }
//                )
//                layoutManager = getLayoutManger(AppConstants.CALENDER_DAYS)
//            }

//            designerBookingPager.apply {
//                adapter = BookingSlotAdapter<Booking, BookingViewModel>(
//                    bookingViewModel,
//                    BookingComparator<Booking>(),
//                    BookingListener<Booking> { slot: Booking, _: Int ->
//
////                        val params = "Project_Key" + "-" +
//////                                "Client_Key" + "-" + bookingViewModel.slotTimeString.value + "-" +
//////                                "Designer_key" + "-" +
//////                                slot.subscriptionName + "-" +
//////                                slot.subscriptionPrice + "-" +
//////                                bookingViewModel.slotDateString.value + "-" +
////                                "booking_Key"
////                        val bookingArgs = Bundle().apply {
////                            putLong("bookingKey", slot.slotId)
////                            putString("bookingParams", params)
////                        }
////                        val navOptions = NavOptions.Builder().setPopUpTo(
////                            R.id.booking_navigation,true ).build()
////                        with(findNavController()) {
//////                            navigate(R.id.payment_navigation, bookingArgs, navOptions)
////                        }
//
//                    },
//                    BookingSlotListener<Booking> { slot: Booking, i: Int, v: String ->
////                        when (slot.slotType) {
////                            0 -> {
////                                Toast.makeText(context, "Basic $v $i",Toast.LENGTH_SHORT ).show()
////                            }
////                            1 -> {
////                                Toast.makeText(context, "Standard $v $i", Toast.LENGTH_SHORT ).show()
////                            }
////                            2 -> {
////                                Toast.makeText(context, "Premium $v $i ", Toast.LENGTH_SHORT).show()
////                            }
////                        }
//
//                        bookingViewModel.setSlotTime(v)
//                        bookingViewModel.setSlotSelected(i)
//                        (binding.designerBookingPager
//                            .adapter as BookingSlotAdapter<Booking, BookingViewModel>)
//                            .notifyDataSetChanged()
//                    }
//                )
////                adapter = BookingSlotAdapter<BookingSlotEntity, BookingViewModel>(
////                    bookingViewModel, BookingComparator<BookingSlotEntity>(),
////                    BookingListener<BookingSlotEntity> { slot: BookingSlotEntity, _: Int ->
////
////                        val params = "Project_Key" + "-" +
////                                     "Client_Key" + "-" + bookingViewModel.slotTimeString.value + "-" +
////                                     "Designer_key" + "-" +
////                                     slot.subscriptionName + "-" +
////                                     slot.subscriptionPrice + "-" +
////                                     bookingViewModel.slotDateString.value + "-" +
////                                "booking_Key"
////                        val bookingArgs = Bundle().apply {
////                            putLong("bookingKey", slot.slotId)
////                            putString("bookingParams", params)
////                        }
////                        val navOptions = NavOptions.Builder().setPopUpTo(
////                            R.id.booking_navigation,true ).build()
////                        with(findNavController()) {
////                            navigate(R.id.payment_navigation, bookingArgs, navOptions)
////                        }
////                    },
////                    BookingSlotListener<BookingSlotEntity> { slot: BookingSlotEntity, i: Int, v: String ->
////                        when (slot.slotType) {
////                            0 -> {
////                                Toast.makeText(context, "Basic $v $i",Toast.LENGTH_SHORT ).show()
////                            }
////                            1 -> {
////                                Toast.makeText(context, "Standard $v $i", Toast.LENGTH_SHORT ).show()
////                            }
////                            2 -> {
////                                Toast.makeText(context, "Premium $v $i ", Toast.LENGTH_SHORT).show()
////                            }
////                        }
////
////                        bookingViewModel.setSlotTime(v)
////                        bookingViewModel.setSlotSelected(i)
////                        (binding.designerBookingPager
////                            .adapter as BookingSlotAdapter<BookingSlotEntity, BookingViewModel>)
////                            .notifyDataSetChanged()
////                    }
////                )
//
//            }

            lifecycleOwner = this@BookingFragment

//            viewModel = bookingViewModel

            viewModel = scheduleViewModel

            invalidateAll()

        }

        scheduleViewModel.slotData.observe(viewLifecycleOwner, { data ->
            if(data !=null) {
             Toast.makeText( context, "data "+data[0].hour+"_"+data[0].available,
                    Toast.LENGTH_SHORT ).show()
                (binding.recyclerViewAvailableTime.
                  adapter as SchedulingAdapter<Slot, SchedulingViewModel>).submitList(data)
                (binding.recyclerViewAvailableTime.
                  adapter as SchedulingAdapter<Slot, SchedulingViewModel>).notifyDataSetChanged()
            }
        })

//        scheduleViewModel.loadingScheduling(getNextBusinessDay(),0L)
//            .observe(this, object : Observer<List<Slot?>?>() {
//                fun onChanged(@Nullable hours: List<Slot?>?) {
////                    availableHours.clear()
////                    availableHours.addAll(hours)
////                    mAdapter.notifyDataSetChanged()
//                }
//            })

//        bookingViewModel.slotDateId.observe(viewLifecycleOwner,
//            androidx.lifecycle.Observer {
//                val c = Calendar.getInstance()
//                val day = c.get(Calendar.DAY_OF_MONTH)
//                val index = it - day + 3
////                Toast.makeText(  context, "1Subscription selectedDate $index",
////                    Toast.LENGTH_SHORT ).show()
//                bookingViewModel.designerSlot.value?.let { it1 -> setSubscriptionSlots(it1, index) }
////                (binding.designerBookingPager
////                    .adapter as BookingSlotAdapter<SlotData.kt, BookingViewModel>)
////                    .data = getSlotDateTime(
////                    bookingViewModel.designerSlot.value,
//////                    bookingViewModel.datePosition.value)
////                    index/*, day.toString()*/)
////                setSubscriptionSlots()
//            })
//
//        bookingViewModel.getUserSubscriptionSlotDates(args.designerKey)
//        bookingViewModel.designerSubscriptionSlotDates.observe(viewLifecycleOwner,
//            {
//                if (it != null) {
//                    Log.d("fragment_Subscription", "user Subscription "+
//                            it[0].subscription.name+"_"+it[0].subscription.price)
//                    initializeCalender(it)
//                }
//            }
//        )
//
//        bookingViewModel.getUserSubscriptionSlotTime(args.designerKey)
//        bookingViewModel.designerSlot.observe(viewLifecycleOwner,
//            {
//                if (it != null) {
//                    Log.d("fragment_SubsSlot", "user SubsSlot "+
//                            it[0].slot[0].date+"_"+it[0].slot[0].time)
//                    bookingViewModel.datePosition.value?.let { it1 -> setSubscriptionSlots(it,it1) }
//                }
//            }
//        )
//
//        //set tabs
//        TabLayoutMediator(binding.designerBookingTab, binding.designerBookingPager) {
//                tab, position -> tab.text = "${
//                            bookingViewModel.designerSlot.value?.
//                                   get(position)?.subscription?.name} Slots"
//        }.attach()

//        binding.designerBookingTab.addOnTabSelectedListener(object :
//            TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
////                Toast.makeText(context, "Designer ${tab.text}", Toast.LENGTH_SHORT).show()
////                binding.designerBookingPager.currentItem = tab.position
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//
//        binding.designerBookingCalender.setOnDateChangeListener(
//                OnDateChangeListener { view, year, month, dayOfMonth ->
//                    val Date = (dayOfMonth.toString() + "-"+ (month + 1) + "-" + year)
//                    // set this date in TextView for Display
//                    Toast.makeText(context, "Designer Date $Date", Toast.LENGTH_SHORT).show()
//
//                }
//        )
//
//        designerViewModel.navigateToProjectDetail.observe(viewLifecycleOwner,
//            {
//                Toast.makeText(context, "Designer $it", Toast.LENGTH_SHORT).show()
//                designerViewModel.onProjectDetailNavigated()
//            })
//
    }

//    override fun onDateSelected(date: CalendarDate?) {
//        if (date != null) {
//            binding.activityMainTextDayOfMonth.text = date.dayToString()
//        }
//        if (date != null) {
//            binding.activityMainTextDayOfWeek.text = date.dayOfWeekToStringName()
//        }
//    }

//    private fun initializeCalender(it: List<SubscriptionSlotDate>) {
////        Toast.makeText(
////            context, "it ${it.size}" +
////                    "Sub ${it[0].subscription.name}" +
////                    "Slot ${it[0].slotDate.size}" +
////                    "Date ${it[0].slotDate[0]}",
////            Toast.LENGTH_LONG
////        ).show()
////
////        numberOfDays = it[0].slotDate.size/basicSlotSize
////        this.startDate = it[0].slotDate[0]
//        val startDate =  getDayTom().get(Calendar.DAY_OF_MONTH).toString()
//        val calendarEntityList = getInitialCalendarDaysData(/*it, null,*/ startDate)
//
//        //set recycler calender
////        binding.monthYearView.text = calendarEntityList[0].month + ", " + calendarEntityList[0].year
////        (binding.bookingCalendarView
////                .adapter as CalenderViewAdapter<CalendarEntity, BookingViewModel>)
////                .data = calendarEntityList
//
//    }
//
//

//    private fun getDayTom(): Calendar {
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.DAY_OF_YEAR, 1)
//        return calendar
//    }
//
//    private fun getInitialCalendarDaysData(/*it: List<SlotCalenderEntity>, selectedDate: String?,*/
//        startDate: String) : List<CalendarEntity>
//    {
////        val formattedSelectedDate = getCalendarDate(
////            if(!TextUtils.isEmpty(selectedDate))
////                selectedDate
////            else
////                startDate
////        )
//        val calendarWeekList = mutableListOf<CalendarEntity>()
//        val sdf = SimpleDateFormat("EEE/dd/MMM/yyyy", Locale.ENGLISH)
//        for (i in 0 until AppConstants.CALENDER_DAYS) {
////            val calendar = getCalendarDate(it[0].slotDate[i])
//            val calendar = getCalendarDate(startDate)
////            val calendar = formattedSelectedDate
//            calendar.add(Calendar.DATE, i)
//            val day = sdf.format(calendar.time)
//            val dateArray = day.split("/")
//            calendarWeekList.add(
//                CalendarEntity( day, dateArray[0], dateArray[1],dateArray[2], dateArray[3],false)
//            )
//        }
//        calendarWeekList[1].isSelected = true
//        bookingViewModel.selectedSlotDate(calendarWeekList[1].fullDate)
//        return calendarWeekList
//    }

//    private fun getCalendarDate(startDate: String) : Calendar
//    {
//        val calendar: Calendar = GregorianCalendar()
//        try {
//            if (!TextUtils.isEmpty(startDate)) {
//                val startDateFormatting = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
//                val formattedStartDate: Date? = startDateFormatting.parse(startDate)
//                calendar.time = formattedStartDate
//            }
//        } catch (ex: Exception) {
//
//        } finally {
//            return calendar
//        }
//    }

//    private fun setSubscriptionSlots(it: List<SubscriptionSlot>, index:Int) {
////        Toast.makeText(  context, "Designer Subscription" +
////                    " Size ${it.size}" +
////                    " provider ${it[0].subscription.designerId}" +
////                    " Name ${it[0].subscription.name}" +
////                    " Slot time ${it[0].slot[0].time}" +
////                    " Slot Basic size  ${it[0].slot.size} " +
////                    " Slot Standard size  ${it[1].slot.size} "+
////                    " Slot Premium size  ${it[2].slot.size} "
////                    ,
////            Toast.LENGTH_SHORT
////        ).show()
//
////        binding.designerBookingPager.currentItem
//        (binding.designerBookingPager
//            .adapter as BookingSlotAdapter<BookingSlotEntity, BookingViewModel>)
//            .data = getSlotDateTime(it, index/*, ""*/)
//
//    }

//    private fun getSlotDateTime(it: List<SubscriptionSlot>?, selectedDate: Int?/*, startDate: String?*/)
//            : List<BookingSlotEntity> {
//        val slotList = mutableListOf<BookingSlotEntity>()
//        val slotListSelectedBasic = mutableListOf<Boolean>()
//        val slotListSelectedStandard = mutableListOf<Boolean>()
//        val slotListSelectedPremium = mutableListOf<Boolean>()
////        Toast.makeText(  context, "2Subscription selectedDate $selectedDate",
////            Toast.LENGTH_SHORT ).show()
//        if (it != null) {
//
//            var index = (selectedDate?.times(4))
//            if (index != null) {
//                slotListSelectedBasic.add(false)
//                slotListSelectedBasic.add(false)
//                slotListSelectedBasic.add(false)
//                slotListSelectedBasic.add(false)
//                slotList.add( BookingSlotEntity(
//                    it[0].subscription.userId,
//                    it[0].subscription.id,
//                    it[0].subscription.name!!,
//                    it[0].subscription.price,
//                    it[0].slot[index + 0].id, 0,
//                    it[0].slot[index + 0].date,
//                    it[0].slot[index + 0].time,
//                    it[0].slot[index + 1].time,
//                    it[0].slot[index + 2].time,
//                    it[0].slot[index + 3].time,
//                    slotListSelectedBasic
//                )
//                )
//            }
//
//            index = (selectedDate?.times(4))
//            if (index != null) {
//                slotListSelectedStandard.add(false)
//                slotListSelectedStandard.add(false)
//                slotListSelectedStandard.add(false)
//                slotListSelectedStandard.add(false)
//                slotList.add(
//                    BookingSlotEntity(
//                    it[1].subscription.userId,
//                    it[1].subscription.id,
//                    it[1].subscription.name!!,
//                    it[1].subscription.price,
//                    it[1].slot[index + 0].id, 1,
//                    it[1].slot[index + 0].date,
//                    it[1].slot[index + 0].time,
//                    it[1].slot[index + 1].time,
//                    it[1].slot[index + 2].time,
//                    it[1].slot[index + 3].time,
//                    slotListSelectedStandard
//                )
//                )
//            }
//
//            index = (selectedDate?.times(2))
//            if (index != null) {
//                slotListSelectedPremium.add(false)
//                slotListSelectedPremium.add(false)
//                slotList.add(
//                    BookingSlotEntity(
//                        it[2].subscription.userId,
//                        it[2].subscription.id,
//                        it[2].subscription.name!!,
//                        it[2].subscription.price,
//                        it[2].slot[index + 0].id, 2,
//                        it[2].slot[index + 0].date,
//                        it[2].slot[index + 0].time,
//                        it[2].slot[index + 1].time,
//                        "",
//                        "",
//                        slotListSelectedPremium
//                    )
//                )
//            }
//
//        }
//
//        return slotList
//    }

    private fun getLayoutManger(daysCount: Int = 7): GridLayoutManager {
        val columnSize = 1
        val layoutManager = GridLayoutManager(
            context, daysCount,
            GridLayoutManager.VERTICAL, false
        )
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return columnSize
            }
        }
        return layoutManager
    }

    companion object {
        const val TAG = "BookingFragment"

        val EXTRA_PROFESSIONAL = "Professional"
    }

}

