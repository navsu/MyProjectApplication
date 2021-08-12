package com.memandis.remote.utils.customview.calender.binding

import android.view.View
import android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.memandis.remote.utils.customview.calender.*
import java.util.*


class CalendarViewPagerAdapter(private val mData: MutableList<CalendarMonth>,
                               private val mViewPager: ViewPager)
    :  PagerAdapter(), OnDayViewClickListener {

    private var mSelectedDate: CalendarDate
    private var mListener: OnDateSelectedListener? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val month = mData[position]
        val monthView = CalendarMonthView(container.context)
        monthView.setSelectedDate(mSelectedDate)
        monthView.setOnDayViewClickListener(this)
        monthView.buildView(month)
        container.addView(monthView, 0)
        monthView.tag = month
        return monthView
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        val view = `object` as View
        val month = view.tag as CalendarMonth
        val position = mData.indexOf(month)
        return if (position >= 0) {
            position
        } else {
            POSITION_NONE
        }
    }

    fun addNext(month: CalendarMonth) {
        mData.add(month)
        notifyDataSetChanged()
    }

    fun addPrev(month: CalendarMonth) {
        mData.add(0, month)
        notifyDataSetChanged()
    }

    fun getItemPageHeader(position: Int): String {
        return mData[position].toString()
    }

    fun getItem(position: Int): CalendarMonth {
        return mData[position]
    }

    fun setOnDateSelectedListener(listener: OnDateSelectedListener?) {
        mListener = listener
        if (mListener != null) {
            mListener!!.onDateSelected(CalendarDate(mSelectedDate))
        }
    }

    override fun onDayViewClick(view: CalendarDayView?) {
        // unset old selection
        decorateSelection(mSelectedDate.toString(), false)

        // set new selection
        mSelectedDate = view!!.date
        decorateSelection(mSelectedDate.toString(), true)
        if (mListener != null) {
            mListener!!.onDateSelected(CalendarDate(mSelectedDate))
        }
    }

    private fun decorateSelection(tag: String, isSelected: Boolean) {
        val output = ArrayList<View>()
        mViewPager.findViewsWithText(output, tag, FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        for (outputView in output) {
            val dayView = outputView as CalendarDayView
            if (isSelected) {
                dayView.setPurpleSolidOvalBackground()
            } else {
                dayView.unsetPurpleSolidOvalBackground()
            }
        }
    }

    init {
        mSelectedDate = CalendarDate(Calendar.getInstance())
    }
}