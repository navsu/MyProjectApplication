package com.memandis.remote.utils.bookingutils

import java.text.SimpleDateFormat
import java.util.*

    fun geIdWithFormattedDateWithoutHours(calendar: Calendar?, vararg ids: String): String? {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dateFormatted: String = dateFormat.format(calendar!!.time)
        var result = ""
        for (id in ids) {
            if (result.isNotEmpty()) {
                result += "_"
            }
            result += id
        }
        return result + "_" + dateFormatted
    }
