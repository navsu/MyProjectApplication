package com.memandis.remote.utils.customview.calender

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RestrictTo
import java.util.*


data class EventDay(val calendar: Calendar) {
    //An object which contains image to display in the day row
    internal var imageDrawable: EventImage = EventImage.EmptyEventImage

    internal var labelColor: Int = 0

    @set:RestrictTo(RestrictTo.Scope.LIBRARY)
    var isEnabled: Boolean = false

    init {
//        calendar.setMidnight()
    }

    constructor(day: Calendar, drawable: Drawable) : this(day) {
        imageDrawable = EventImage.EventImageDrawable(drawable)
    }

    constructor(day: Calendar, @DrawableRes drawableRes: Int) : this(day) {
        imageDrawable = EventImage.EventImageResource(drawableRes)
    }

    constructor(day: Calendar, @DrawableRes drawableRes: Int, @ColorInt labelColor: Int) : this(day) {
        imageDrawable = EventImage.EventImageResource(drawableRes)
        this.labelColor = labelColor
    }
}

class Event {
    var color: Int
        private set
    var timeInMillis: Long
        private set

    @get:Nullable
    var data: Any? = null
        private set

    constructor(color: Int, timeInMillis: Long) {
        this.color = color
        this.timeInMillis = timeInMillis
    }

    constructor(color: Int, timeInMillis: Long, data: Any?) {
        this.color = color
        this.timeInMillis = timeInMillis
        this.data = data
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val event = o as Event
        if (color != event.color) return false
        if (timeInMillis != event.timeInMillis) return false
        return if (if (data != null) data != event.data else event.data != null) false else true
    }

    override fun hashCode(): Int {
        var result = color
        result = 31 * result + (timeInMillis xor (timeInMillis ushr 32)).toInt()
        result = 31 * result + if (data != null) data.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "Event{" +
                "color=" + color +
                ", timeInMillis=" + timeInMillis +
                ", data=" + data +
                '}'
    }
}

class Events(val timeInMillis: Long, events: List<Event>?) {
    private val events: List<Event>?

    fun getEvents(): List<Event>? {
        return events
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val event = o as Events
        if (timeInMillis != event.timeInMillis) return false
        return if (if (events != null) events != event.events else event.events != null) false else true
    }

    override fun hashCode(): Int {
        var result = events?.hashCode() ?: 0
        result = 31 * result + (timeInMillis xor (timeInMillis ushr 32)).toInt()
        return result
    }

    override fun toString(): String {
        return "Events{" +
                "events=" + events +
                ", timeInMillis=" + timeInMillis +
                '}'
    }

    init {
        this.events = events
    }

}

class EventComparator : Comparator<Event> {
    override fun compare(lhs: Event, rhs: Event): Int {
        return if (lhs.timeInMillis < rhs.timeInMillis) -1 else if (lhs.timeInMillis == rhs.timeInMillis) 0 else 1
    }
}

class EventsContainer(private val eventsCalendar: Calendar) {
    private val eventsByMonthAndYearMap: MutableMap<String, MutableList<Events>> = HashMap()
    private val eventsComparator: Comparator<Event> = EventComparator()
    fun addEvent(event: Event) {
        eventsCalendar.timeInMillis = event.timeInMillis
        val key = getKeyForCalendarEvent(eventsCalendar)
        var eventsForMonth = eventsByMonthAndYearMap[key]
        if (eventsForMonth == null) {
            eventsForMonth = ArrayList()
        }
        val eventsForTargetDay = getEventDayEvent(event.timeInMillis)
        if (eventsForTargetDay == null) {
            val events: MutableList<Event> = ArrayList()
            events.add(event)
            eventsForMonth.add(Events(event.timeInMillis, events))
        } else {
//            eventsForTargetDay.getEvents().add(event)
        }
        eventsByMonthAndYearMap[key] = eventsForMonth
    }

    fun removeAllEvents() {
        eventsByMonthAndYearMap.clear()
    }

    fun addEvents(events: List<Event>) {
        val count = events.size
        for (i in 0 until count) {
            addEvent(events[i])
        }
    }

    fun getEventsFor(epochMillis: Long): List<Event>? {
        val events = getEventDayEvent(epochMillis)
        return if (events == null) {
            ArrayList()
        } else {
            events.getEvents()
        }
    }

    fun getEventsForMonthAndYear(month: Int, year: Int): List<Events> {
        return eventsByMonthAndYearMap[year.toString() + "_" + month]!!
    }

    fun getEventsForMonth(eventTimeInMillis: Long): List<Event> {
        eventsCalendar.timeInMillis = eventTimeInMillis
        val keyForCalendarEvent = getKeyForCalendarEvent(eventsCalendar)
        val events: List<Events>? =
            eventsByMonthAndYearMap[keyForCalendarEvent]
        val allEventsForMonth: MutableList<Event> = ArrayList()
        if (events != null) {
            for (eve in events) {
                if (eve != null) {
                    allEventsForMonth.addAll(eve.getEvents()!!)
                }
            }
        }
        Collections.sort(allEventsForMonth, eventsComparator)
        return allEventsForMonth
    }

    private fun getEventDayEvent(eventTimeInMillis: Long): Events? {
        eventsCalendar.timeInMillis = eventTimeInMillis
        val dayInMonth = eventsCalendar[Calendar.DAY_OF_MONTH]
        val keyForCalendarEvent = getKeyForCalendarEvent(eventsCalendar)
        val eventsForMonthsAndYear: List<Events>? =
            eventsByMonthAndYearMap[keyForCalendarEvent]
        if (eventsForMonthsAndYear != null) {
            for (events in eventsForMonthsAndYear) {
                eventsCalendar.timeInMillis = events.timeInMillis
                val dayInMonthFromCache = eventsCalendar[Calendar.DAY_OF_MONTH]
                if (dayInMonthFromCache == dayInMonth) {
                    return events
                }
            }
        }
        return null
    }

    fun removeEventByEpochMillis(epochMillis: Long) {
        eventsCalendar.timeInMillis = epochMillis
        val dayInMonth = eventsCalendar[Calendar.DAY_OF_MONTH]
        val key = getKeyForCalendarEvent(eventsCalendar)
        val eventsForMonthAndYear = eventsByMonthAndYearMap[key]
        if (eventsForMonthAndYear != null) {
            val calendarDayEventIterator = eventsForMonthAndYear.iterator()
            while (calendarDayEventIterator.hasNext()) {
                val next = calendarDayEventIterator.next()
                eventsCalendar.timeInMillis = next.timeInMillis
                val dayInMonthFromCache = eventsCalendar[Calendar.DAY_OF_MONTH]
                if (dayInMonthFromCache == dayInMonth) {
                    calendarDayEventIterator.remove()
                    break
                }
            }
            if (eventsForMonthAndYear.isEmpty()) {
                eventsByMonthAndYearMap.remove(key)
            }
        }
    }

    fun removeEvent(event: Event) {
        eventsCalendar.timeInMillis = event.timeInMillis
        val key = getKeyForCalendarEvent(eventsCalendar)
        val eventsForMonthAndYear = eventsByMonthAndYearMap[key]
        if (eventsForMonthAndYear != null) {
            val eventsForMonthYrItr = eventsForMonthAndYear.iterator()
            while (eventsForMonthYrItr.hasNext()) {
                val events = eventsForMonthYrItr.next()
                val indexOfEvent = events.getEvents()!!.indexOf(event)
                if (indexOfEvent >= 0) {
                    if (events.getEvents()!!.size == 1) {
                        eventsForMonthYrItr.remove()
                    } else {
//                        events.getEvents().removeAt(indexOfEvent)
                    }
                    break
                }
            }
            if (eventsForMonthAndYear.isEmpty()) {
                eventsByMonthAndYearMap.remove(key)
            }
        }
    }

    fun removeEvents(events: List<Event>) {
        val count = events.size
        for (i in 0 until count) {
            removeEvent(events[i])
        }
    }

    //E.g. 4 2016 becomes 2016_4
    private fun getKeyForCalendarEvent(cal: Calendar): String {
        return cal[Calendar.YEAR].toString() + "_" + cal[Calendar.MONTH]
    }
}