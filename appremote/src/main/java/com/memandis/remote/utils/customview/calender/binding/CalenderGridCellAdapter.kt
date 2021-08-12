package com.memandis.remote.utils.customview.calender.binding

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.memandis.remote.R
import java.util.*

class GridCellAdapter(private val _context: Context, textViewResourceId: Int, month: Int, year: Int )
    :  BaseAdapter(), View.OnClickListener {

    private val list: MutableList<String> = TODO()
    private val weekdays = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    private val months = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private val daysOfMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val month: Int
    private val year: Int
    var daysInMonth = 0
    var prevMonthDays = 0
    private val currentDayOfMonth: Int
    private var gridCell: Button? = null

    override fun getItem(position: Int): String {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
//        Log.d(SimpleCalender.tag2, "getView ...")
        var row = convertView
        if (row == null) {
            // ROW INFLATION
//            Log.d(SimpleCalender.tag2, "Starting XML Row Inflation ... ")
            val inflater =_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            row = inflater.inflate(R.layout.view_simple_calender_day_gridcell, parent, false)
//            Log.d(SimpleCalender.tag2, "Successfully completed XML Row Inflation!")
        }

            // Get a reference to the Day gridcell
            gridCell = row.findViewById<View>(R.id.day_gridcell) as Button
            gridCell!!.setOnClickListener(this)

            // ACCOUNT FOR SPACING
//        Log.d( SimpleCalender.tag2,"Current Day: $currentDayOfMonth")
            val day_color = list[position].split("-".toRegex()).toTypedArray()
            gridCell!!.text = day_color[0]
            gridCell!!.tag = day_color[0] + "-" + day_color[2] + "-" + day_color[3]
            if (day_color[1] == "GREY") {
                gridCell!!.setTextColor(Color.LTGRAY)
            }
            if (day_color[1] == "WHITE") {
                gridCell!!.setTextColor(Color.WHITE)
            }
            if (position == currentDayOfMonth) {
                gridCell!!.setTextColor(Color.BLUE)
            }
            return row
        }

    override fun onClick(view: View) {
//        val date_month_year = view.tag as String
//        Toast.makeText(applicationContext, date_month_year, Toast.LENGTH_SHORT).show()
//        selectedDayMonthYearButton!!.text = "Selected: $date_month_year"
    }

    private fun printMonth(mm: Int, yy: Int) {
    // The number of days to leave blank at
    // the start of this month.
    var trailingSpaces = 0
    val leadSpaces = 0
    var daysInPrevMonth = 0
    var prevMonth = 0
    var prevYear = 0
    var nextMonth = 0
    var nextYear = 0
    val cal = GregorianCalendar(yy, mm, currentDayOfMonth)

    // Days in Current Month
    daysInMonth = daysOfMonth[mm]
    if (mm == 11) {
        prevMonth = 10
        daysInPrevMonth = daysOfMonth[prevMonth]
        nextMonth = 0
        prevYear = yy
        nextYear = yy + 1
    } else if (mm == 0) {
        prevMonth = 11
        prevYear = yy - 1
        nextYear = yy
        daysInPrevMonth = daysOfMonth[prevMonth]
        nextMonth = 1
    } else {
        prevMonth = mm - 1
        nextMonth = mm + 1
        nextYear = yy
        prevYear = yy
        daysInPrevMonth = daysOfMonth[prevMonth]
    }

    // Compute how much to leave before before the first day of the
    // month.
    // getDay() returns 0 for Sunday.
    trailingSpaces = cal[Calendar.DAY_OF_WEEK] - 1
    if (cal.isLeapYear(cal[Calendar.YEAR]) && mm == 1) {
        ++daysInMonth
    }

    // Trailing Month days
    for (i in 0 until trailingSpaces) {
        list.add((daysInPrevMonth - trailingSpaces + 1 + i).toString() + "-GREY" + "-" + months[prevMonth] + "-" + prevYear)
    }

    // Current Month Days
    for (i in 1..daysInMonth) {
        list.add(i.toString() + "-WHITE" + "-" + months[mm] + "-" + yy)
    }

    // Leading Month days
    for (i in 0 until list.size % 7) {
//            Log.d(SimpleCalender.tag2, "NEXT MONTH:= " + months[nextMonth])
        list.add((i + 1).toString() + "-GREY" + "-" + months[nextMonth] + "-" + nextYear)
    }
}

}