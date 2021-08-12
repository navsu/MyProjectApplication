package com.memandis.remote.utils.customview.calender

import com.memandis.remote.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import com.memandis.remote.utils.app.AppUtils

class CalendarMonthView(context: Context) : FrameLayout(context), View.OnClickListener {

    private var mGridLayout: GridLayout? = null
    private var mLayoutDays: ViewGroup? = null
    private var mListener: OnDayViewClickListener? = null
    private var mSelectedDate: CalendarDate? = null

    fun setOnDayViewClickListener(listener: OnDayViewClickListener?) {
        mListener = listener
    }

    fun setSelectedDate(selectedDate: CalendarDate?) {
        mSelectedDate = selectedDate
    }

    private fun init() {
        inflate(context, R.layout.view_calendar_month, this)
        mGridLayout = findViewById<View>(R.id.view_calendar_month_grid) as GridLayout
        mLayoutDays = findViewById<View>(R.id.view_calendar_month_layout_days) as ViewGroup
    }

    fun buildView(calendarMonth: CalendarMonth) {
        buildDaysLayout()
        buildGridView(calendarMonth)
    }

    private fun buildDaysLayout() {
        val days: Array<String> = resources.getStringArray(R.array.days_sunday_array)
        for (i in 0 until mLayoutDays!!.childCount) {
            val textView = mLayoutDays!!.getChildAt(i) as TextView
            textView.text = days[i]
        }
    }

    private fun buildGridView(calendarMonth: CalendarMonth) {
        val row: Int = CalendarMonth.NUMBER_OF_WEEKS_IN_MONTH
        val col: Int = CalendarMonth.NUMBER_OF_DAYS_IN_WEEK
        mGridLayout!!.rowCount = row
        mGridLayout!!.columnCount = col
        val screenWidth: Int = AppUtils.getScreenWidth(context)
        val width = screenWidth / col
        for (date in calendarMonth.days!!) {
            val params = GridLayout.LayoutParams()
            params.width = width
            params.height = LayoutParams.WRAP_CONTENT
            val dayView = CalendarDayView(context, date as CalendarDate)
            dayView.contentDescription = date.toString()
            dayView.layoutParams = params
            dayView.setOnClickListener(this)
            decorateDayView(dayView, date, calendarMonth.month)
            mGridLayout!!.addView(dayView)
        }
    }

    private fun decorateDayView(dayView: CalendarDayView, day: CalendarDate, month: Int) {
        if (day.month != month) {
            dayView.setOtherMothTextColor()
        } else {
            dayView.setThisMothTextColor()
        }
        if (mSelectedDate != null && mSelectedDate!!.isDateEqual(day)) {
            dayView.setPurpleSolidOvalBackground()
        } else {
            dayView.unsetPurpleSolidOvalBackground()
        }
    }

    override fun onClick(view: View) {
        if (mListener != null) {
            mListener!!.onDayViewClick(view as CalendarDayView)
        }
    }

    init {
        init()
    }
}