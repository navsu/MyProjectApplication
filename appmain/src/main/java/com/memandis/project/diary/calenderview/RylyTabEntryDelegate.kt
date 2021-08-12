package com.memandis.project.diary.calenderview

import android.icu.text.DateFormat
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textview.MaterialTextView
import com.memandis.appmain.R
import com.memandis.project.diary.calenderview.RylyToolbarBehaviorDelegate
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.utils.app.TIME_FORMATTER_SHORT


class RylyTabEntryDelegate(private var onTabClickListener: ((DiaryEntry) -> Unit)? = null) :
    RylyToolbarBehaviorDelegate<DiaryEntry> {

    private val LOG_TAG = this::class.java.simpleName

    override val itemLayoutRes: Int
        get() = R.layout.item_tab_big

    override fun onTabScrolled(
            overlineTextView: MaterialTextView,
            headerTextView: MaterialTextView,
            subtitleTextView: MaterialTextView,
            item: DiaryEntry) {}

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTabSelected(overlineTextView: MaterialTextView,
                               headerTextView: MaterialTextView,
                               subtitleTextView: MaterialTextView,
                               item: DiaryEntry) {

        Log.d(LOG_TAG, "Selected a new entry: ${item.id}\t${item.title}")
        // bigTextView.text = TIME_FORMATTER_FULL.format(currentSelectedTime)

        overlineTextView.apply {
            val time = item.timeCreated
            val timeString = DateFormat.getDateInstance(DateFormat.LONG).format(time)

            val relativeTimeString = DateUtils.getRelativeTimeSpanString(
                    time.time, System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NUMERIC_DATE)

            text = context.getString(R.string.template_dot_2_messages,
                                     relativeTimeString, timeString)
        }
        headerTextView.text = item.title
        if(!item.subtitle.isNullOrEmpty()) {
            subtitleTextView.text = item.subtitle
        } else {
            subtitleTextView.visibility = View.GONE
        }

        onTabClickListener?.let { it(item) }
        // bigTextView.typeface
        // subtitleTextView.text = DateUtils.getRelativeTimeSpanString(
        //         currentSelectedTime.time, System.currentTimeMillis(),
        //         DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
    }

    override fun onCreateTab(tab: TabLayout.Tab, itemIndex: Int, item: DiaryEntry) {
        val goodness = tab.view.findViewById<TextView>(R.id.goodnessPointTextView)
        // if(itemIndex == 0){
        //     // tab.view.rootView.updatePaddingRelative(start = 16.toPx)
        //     tab.view.rootView.setMargins(l = 16.toPx)
        // }
        println("tab.text ==> #${itemIndex + 1}")
        tab.text = "#${(itemIndex + 1)}"
        goodness.text = TIME_FORMATTER_SHORT.format(item.timeCreated)
    }

    override fun isAwaysEmphasized(item: DiaryEntry): Boolean? {
        return false
    }

    override fun onOverlineTextClickListener(textView: View, item: DiaryEntry) {
        // TODO("Not yet implemented")
    }
}