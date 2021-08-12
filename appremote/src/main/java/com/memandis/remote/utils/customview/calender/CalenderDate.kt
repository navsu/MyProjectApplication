package com.memandis.remote.utils.customview.calender

import java.util.*


class CalendarDate internal constructor(
    /**
     * Return the value representation of the day of month in this calendar.
     *
     * @return day of month value
     */
    var day: Int,
    /**
     * Return the value representation of the month in this calendar.
     * The month value is 0-based: 0 for January, 11 for December.
     *
     * @return month value
     */
    var month: Int, year: Int
) {
    /**
     * Return the value representation of the year in this calendar.
     *
     * @return year value
     */
    var year: Int
        private set

    /**
     * Constructor
     *
     * @param calendar is a [java.util.Calendar] object.
     */
    constructor(calendar: Calendar) : this(
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.YEAR)
    ) {
    }

    /**
     * Constructor
     * This is a copy constructor.
     *
     * @param other is another instance of CalendarDate.
     */
    constructor(other: CalendarDate) : this(
        other.day,
        other.month,
        other.year
    ) {
    }

    /**
     * Get this calendar object in a [java.util.Calendar] format.
     * The time of this calendar is set to 00:00:00 with 0 milliseconds
     *
     * @return this calendar as [java.util.Calendar].
     */
    val calender: Calendar
        get() {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(year, month, day)
            return calendar
        }

    /**
     * Returns this Calendar time value in milliseconds.
     * The hour time of this calendar is set to 00:00:00 with 0 milliseconds
     *
     * @return the current time as UTC milliseconds from the epoch.
     */
    val millis: Long
        get() = calender.timeInMillis
    val dayOfWeek: Int
        get() = calender.get(Calendar.DAY_OF_WEEK)

    /**
     * Returns if this date of this calendar is the same date as today.
     *
     * @return true if the calendar represent the day of today.
     */
    val isToday: Boolean
        get() {
            val today: Calendar = Calendar.getInstance()
            return year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH) && day == today.get(
                Calendar.DAY_OF_MONTH
            )
        }

    fun isDateEqual(other: CalendarDate): Boolean {
        return year == other.year && month == other.month && day == other.day
    }

    /**
     * Adds or subtracts the specified amount of days to the calendar.
     *
     * @param value the amount of days added (set a negative value for subtraction).
     */
    fun addDays(value: Int) {
        val calendar: Calendar = calender
        calendar.add(Calendar.DATE, value)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    /**
     * Return a string representation of this calendar.
     *
     * @return string in a format of dd/mm/yyy
     */
    override fun toString(): String {
        return dayToString() + "/" + monthToString() + "/" + yearToString()
    }

    /**
     * Return a string representation of the day of month in this calendar.
     *
     * @return String of the day of month number in a format of dd
     */
    fun dayToString(): String {
        return if (day < 10) {
            "0$day"
        } else {
            "" + day
        }
    }

    /**
     * Return a string representation of the month in this calendar.
     *
     * @return String of the month number in a format of mm
     */
    private fun monthToString(): String {
        return if (month + 1 < 10) {
            "0" + (month + 1)
        } else {
            "" + (month + 1)
        }
    }

    /**
     * Return a string representation of the year in this calendar.
     *
     * @return String of the year number in a format of yyyy.
     */
    private fun yearToString(): String {
        return year.toString()
    }

    /**
     * Return a string representation of the month name in this calendar.
     *
     * @return the month name like it appears in the Julian and Gregorian calendars as a string.
     */
    fun monthToStringName(): String? {
        return DateUtils.monthToString(month)
    }

    /**
     * Return a string representation of the day of week name in this calendar.
     *
     * @return the day of week name like it appears in the Julian and Gregorian calendars as a string.
     */
    fun dayOfWeekToStringName(): String? {
        return DateUtils.dayOfWeekToString(calender.get(Calendar.DAY_OF_WEEK))
    }

    /**
     * Constructor
     *
     * @param day   is the number of day in the month.
     * @param month value is 0-based: 0 for January, 11 for December.
     * @param year
     */
    init {
        month = month
        this.year = year
    }
}