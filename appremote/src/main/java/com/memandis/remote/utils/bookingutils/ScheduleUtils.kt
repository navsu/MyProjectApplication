package com.memandis.remote.utils.bookingutils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

    private const val DEFAULT_DATE_FORMAT = "dd-MM-yyyy HH:mm"

    fun getNextBusinessDay(): Calendar? {
        val calendar: Calendar = Calendar.getInstance()
        if (calendar.get(Calendar.DAY_OF_WEEK) === Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, 2)
        } else if (calendar.get(Calendar.DAY_OF_WEEK) === Calendar.SUNDAY ||
            calendar.get(Calendar.HOUR_OF_DAY) > 18
        ) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
        return calendar
    }

    fun parseDate(source: String): Date? {
        val dateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT)
        try {
            return dateFormat.parse(source)
        } catch (e: Exception) {
        }
        return null
    }
