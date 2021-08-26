package com.memandis.remote.utils.bookingutils

import android.text.format.DateUtils
import com.memandis.remote.datasource.model.booking.Slot
import java.util.*
import kotlin.collections.ArrayList



    private val hours = intArrayOf(9, 10, 11, 13, 14, 15, 16, 17)

    fun generateSlots(calendar: Calendar): List<Slot>? {
        val list: MutableList<Slot> = ArrayList()
        val today: Boolean = DateUtils.isToday(calendar.getTimeInMillis())
        val currentHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        for (hour in hours) {
            if (today && currentHour > hour) {
                list.add(
                    Slot(0L, 0L, 0L, "",0L,"",
                    hour, available = false)
                )
            } else {
                list.add(
                    Slot(0L, 0L, 0L, "",0L,"",
                    hour, available = true)
                )
            }
        }
        return list
    }

