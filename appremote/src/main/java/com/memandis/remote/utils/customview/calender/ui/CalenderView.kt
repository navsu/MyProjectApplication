package com.memandis.remote.utils.customview.calender.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.Nullable
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import java.util.*
import com.memandis.remote.R
import com.memandis.remote.utils.customview.calender.CalendarMonth
import com.memandis.remote.utils.customview.calender.EventDay
import com.memandis.remote.utils.customview.calender.OnDateSelectedListener
import com.memandis.remote.utils.customview.calender.binding.CalendarViewPagerAdapter

//
//import android.content.Context
//import android.text.TextUtils
//import android.view.LayoutInflater
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.GridLayoutManager
//import com.memandis.mydezigner.R
//import com.memandis.mydezigner.domain.entities.booking.CalendarEntity
//import com.memandis.mydezigner.main.ui.booking.binding.CalendarComparator
//import com.memandis.mydezigner.main.ui.booking.binding.CalendarListener
//import com.memandis.mydezigner.main.ui.booking.binding.CalenderViewAdapter
//import java.text.SimpleDateFormat
//import java.util.*
//
//class CalendarView @JvmOverloads constructor( context: Context) :  ConstraintLayout(context) {
//
//    private var numberOfDays = 7
//    private var startDate: String? = null
//
//    private var calendarViewBinding: ViewCalendarBinding
//
//
//    init {
//        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        calendarViewBinding = DataBindingUtil.inflate(inflater, R.layout.view_calender, this, true)
//    }
//
//    fun initialize(daysCount: Int = 7, startDate: String? = null, selectedDate: String? = null) {
//        numberOfDays = daysCount
//        this.startDate = startDate
//
//        calendarEntityList = getCalendarData(daysCount, selectedDate, startDate)
//        calendarViewAdapter = CalenderViewAdapter<CalendarEntity>(
//                                         R.layout.calender_day_date_view,
//                                          CalendarComparator<CalendarEntity>(),
//                                           CalendarListener<CalendarEntity>{
//                                                   calendarEntity: CalendarEntity, i: Int ->
//
//                                           }
//        )
//        calendarViewAdapter.setData(calendarEntityList)
//
//        calendarViewBinding.calendarViewAdapter = calendarViewAdapter
//
//        calendarViewBinding.apply {
//            layoutManager = getLayoutManger(daysCount)
//        }
//
//        dateDisplayMonthYear.text = calendarEntityList[0].month + ", " + calendarEntityList[0].year
//    }
//
//    fun getCalendarData(numberOfDays: Int, selectedDate: String?, startDate: String?): List<CalendarEntity> {
//        val calendarWeekList = mutableListOf<CalendarEntity>()
//        val sdf = SimpleDateFormat("EEE/dd/MMM/yyyy", Locale.ENGLISH)
//
//        val formattedSelectedDate = getCalendarDate(if(!TextUtils.isEmpty(selectedDate)) selectedDate else startDate)
//        for (i in 0 until numberOfDays) {
//            val calendar = getCalendarDate(startDate)
//            calendar.add(Calendar.DATE, i)
//            val day = sdf.format(calendar.time)
//            val dateArray = day.split("/")
//            calendarWeekList.add(CalendarEntity(day, dateArray[0], dateArray[1], dateArray[2], dateArray[3]))
//        }
//
//        return calendarWeekList
//    }
//
//    private fun getCalendarDate(startDate: String?) : Calendar {
//        val calendar: Calendar = GregorianCalendar()
//        try {
//            if (!TextUtils.isEmpty(startDate)) {
//                val startDateFormating = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
//                val formattedStartDate: Date = startDateFormating.parse(startDate)
//                calendar.time = formattedStartDate
//            }
//        } catch (ex: Exception) {
//
//        } finally {
//            return calendar
//        }
//    }
//
//    private fun getLayoutManger(daysCount: Int = 7): GridLayoutManager {
//        val column_size = 1
//        val layoutManager = GridLayoutManager(context, daysCount, GridLayoutManager.VERTICAL, false)
//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return column_size
//            }
//        }
//        return layoutManager
//    }
//
//}

class CustomCalendarView : FrameLayout, View.OnClickListener {
    private var mPagerTextMonth: TextView? = null
    private var mButtonLeftArrow: ImageButton? = null
    private var mButtonRightArrow: ImageButton? = null
    private var mViewPager: ViewPager? = null

    private var mViewPagerAdapter: CalendarViewPagerAdapter? = null
//    private val mListener: OnDateSelectedListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflate(context, R.layout.view_custom_calendar, this)
        mViewPager = findViewById<View>(R.id.calender_view_pager) as ViewPager
        mPagerTextMonth = findViewById<View>(R.id.calender_pager_text_month) as TextView
        mButtonLeftArrow = findViewById<View>(R.id.calender_pager_button_left_arrow) as ImageButton
        mButtonRightArrow = findViewById<View>(R.id.calender_pager_button_right_arrow) as ImageButton
        mButtonLeftArrow!!.setOnClickListener(this)
        mButtonRightArrow!!.setOnClickListener(this)
        buildCalendarView()
    }
    fun setOnDateSelectedListener(listener: OnDateSelectedListener?) {
        mViewPagerAdapter?.setOnDateSelectedListener(listener)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.calender_pager_button_right_arrow -> {
                val next = mViewPager!!.currentItem + 1
                mViewPager!!.setCurrentItem(next, true)
            }
            R.id.calender_pager_button_left_arrow -> {
                val prev = mViewPager!!.currentItem - 1
                mViewPager!!.setCurrentItem(prev, true)
            }
        }
    }

    fun setEvents(eventDays: List<EventDay>) {
//        if (calendarProperties.eventsEnabled) {
//            calendarProperties.eventDays = eventDays
//            calendarPageAdapter.notifyDataSetChanged()
//        }
    }

    private fun buildCalendarView() {
        val list: MutableList<CalendarMonth> = ArrayList()
        val today = CalendarMonth(Calendar.getInstance())
        list.add(CalendarMonth(today, -2))
        list.add(CalendarMonth(today, -1))
        list.add(today)
        list.add(CalendarMonth(today, 1))
        list.add(CalendarMonth(today, 2))

        mViewPagerAdapter = mViewPager?.let { CalendarViewPagerAdapter(list, it) }
        mViewPager!!.adapter = mViewPagerAdapter
        mViewPager!!.addOnPageChangeListener(mPageChangeListener)
        mViewPager!!.offscreenPageLimit = 1
        mViewPager!!.currentItem = 2
        mPagerTextMonth?.text = mViewPagerAdapter!!.getItemPageHeader(2)
    }

    private val mPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {}
        override fun onPageScrollStateChanged(state: Int) {
            val position = mViewPager!!.currentItem
            mPagerTextMonth?.text = mViewPagerAdapter?.getItemPageHeader(position)

            // current item is the first item in the list
            if (state == ViewPager.SCROLL_STATE_IDLE && position == 1) {
                addPrev()
            }

            // current item is the last item in the list
            if (state == ViewPager.SCROLL_STATE_IDLE &&
                position == (mViewPagerAdapter!!.count - 2)) {
                addNext()
            }
        }
    }

    private fun addNext() {
        val pos: Int? = mViewPagerAdapter?.count?.minus(1)
        val month: CalendarMonth? = pos?.let { mViewPagerAdapter!!.getItem(it) }
        month?.let { CalendarMonth(it, 1) }?.let { mViewPagerAdapter?.addNext(it) }
    }

    private fun addPrev() {
        val month: CalendarMonth? = mViewPagerAdapter?.getItem(0)
        month?.let { CalendarMonth(it, -1) }?.let { mViewPagerAdapter?.addPrev(it) }
    }

}