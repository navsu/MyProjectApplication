package com.memandis.remote.utils.customview.calender

import com.memandis.remote.R
import com.memandis.remote.utils.app.UtilsApp

import java.util.*

object DateUtils {
    /**
     * Return a string with the month name like it appears in the Julian and Gregorian calendars.
     *
     * @param month value is 0-based: 0 for January, 11 for December.
     * @return the month name like it appears in the Julian and Gregorian calendars as a string.
     */
    fun monthToString(month: Int): String? {
        when (month) {
            Calendar.JANUARY -> return UtilsApp.getApp()?.resources?.getString(R.string.january)
            Calendar.FEBRUARY -> return UtilsApp.getApp()?.resources?.getString(R.string.february)
            Calendar.MARCH -> return UtilsApp.getApp()?.resources?.getString(R.string.march)
            Calendar.APRIL -> return UtilsApp.getApp()?.resources?.getString(R.string.april)
            Calendar.MAY -> return UtilsApp.getApp()?.resources?.getString(R.string.may)
            Calendar.JUNE -> return UtilsApp.getApp()?.resources?.getString(R.string.june)
            Calendar.JULY -> return UtilsApp.getApp()?.resources?.getString(R.string.july)
            Calendar.AUGUST -> return UtilsApp.getApp()?.resources?.getString(R.string.august)
            Calendar.SEPTEMBER -> return UtilsApp.getApp()?.resources?.getString(R.string.september)
            Calendar.OCTOBER -> return UtilsApp.getApp()?.resources?.getString(R.string.october)
            Calendar.NOVEMBER -> return UtilsApp.getApp()?.resources?.getString(R.string.november)
            Calendar.DECEMBER -> return UtilsApp.getApp()?.resources?.getString(R.string.december)
        }
        return ""
    }

    /**
     * Return a string with the day name like it appears in the Julian and Gregorian calendars.
     *
     * @param day is the day of the week value.
     * @return the day of week name like it appears in the Julian and Gregorian calendars as a string.
     */
    fun dayOfWeekToString(day: Int): String? {
        when (day) {
            Calendar.SUNDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.sunday)
            Calendar.MONDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.monday)
            Calendar.TUESDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.wednesday)
            Calendar.THURSDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.thursday)
            Calendar.FRIDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.friday)
            Calendar.SATURDAY -> return UtilsApp.getApp()?.resources?.getString(R.string.saturday)
        }
        return ""
    }

    /**
     * Return a int with the days in month like it appears in the Julian and Gregorian calendars.
     *
     * @param month value is 28, 30 or 31: 31 for January, 28 for February, 30 for April
     * @return the days of month like it appears in the Julian and Gregorian calendars as a int.
     */
    fun daysOfMonth(month: Int): Int? {
        when (month) {
            Calendar.JANUARY -> return 31
            Calendar.FEBRUARY -> return 28
            Calendar.MARCH -> return 31
            Calendar.APRIL -> return 30
            Calendar.MAY -> return 31
            Calendar.JUNE -> return 30
            Calendar.JULY -> return 31
            Calendar.AUGUST -> return 31
            Calendar.SEPTEMBER -> return 30
            Calendar.OCTOBER -> return 31
            Calendar.NOVEMBER -> return 30
            Calendar.DECEMBER -> return 31
        }
        return 0
    }
}