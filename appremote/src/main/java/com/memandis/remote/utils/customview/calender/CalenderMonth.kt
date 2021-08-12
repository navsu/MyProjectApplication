package com.memandis.remote.utils.customview.calender

import java.util.*


class CalendarMonth {
    var month: Int
        private set
    var year: Int
        private set
    private var mDays: MutableList<CalendarDate>? = null

    constructor(calendar: Calendar) {
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        createMonthDays()
    }

    constructor(other: CalendarMonth, value: Int) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(other.year, other.month, 1)
        calendar.add(Calendar.MONTH, value)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        createMonthDays()
    }

    private fun createMonthDays() {
        val date = CalendarDate(1, month, year)
        val dayOfWeek: Int = date.dayOfWeek
        date.addDays(1 - dayOfWeek)
        mDays = ArrayList<CalendarDate>()
        for (i in 0 until NUMBER_OF_DAYS_IN_WEEK * NUMBER_OF_WEEKS_IN_MONTH) {
            val day = CalendarDate(date.day, date.month, date.year)
            mDays!!.add(day)
            date.addDays(1)
        }
    }

    val days: List<Any>?
        get() = mDays

    override fun toString(): String {
        return DateUtils.monthToString(month).toString() + "  " + year
    }

    companion object {
        const val NUMBER_OF_WEEKS_IN_MONTH = 6
        const val NUMBER_OF_DAYS_IN_WEEK = 7
    }
}