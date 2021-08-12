package com.memandis.remote.utils.customview.calender

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.memandis.remote.R

class CalendarDayView(context: Context?, val date: CalendarDate) : LinearLayout(context) {

    private var mTextDay: TextView? = null
    private var mLayoutBackground: View? = null

    private fun init() {
        inflate(context, R.layout.view_calendar_day, this)
        mLayoutBackground = findViewById(R.id.view_calendar_day_layout_background)
        mTextDay = findViewById<View>(R.id.view_calendar_day_text) as TextView
        mTextDay!!.text = "" + date.day
    }

    fun setThisMothTextColor() {
        mTextDay!!.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    fun setOtherMothTextColor() {
        mTextDay!!.setTextColor(ContextCompat.getColor(context, R.color.Grey100))
    }

    fun setPurpleSolidOvalBackground() {
        mLayoutBackground!!.setBackgroundResource(R.drawable.oval_purple_solid)
    }

    fun unsetPurpleSolidOvalBackground() {
        mLayoutBackground!!.setBackgroundResource(R.drawable.oval_black_solid)
    }

    init {
        init()
    }
}