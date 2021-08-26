package com.memandis.appbooking.widget

import android.content.Intent
import android.widget.RemoteViewsService
import android.widget.RemoteViews

import com.memandis.appbooking.scheduling.SchedulingActivity

import com.memandis.remote.datasource.model.booking.Professional

import android.widget.AdapterView

import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.DatabaseReference

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.FirebaseDatabase
import com.memandis.appbooking.R
import com.memandis.remote.datasource.model.booking.Scheduling
import com.memandis.remote.utils.bookingutils.geIdWithFormattedDateWithoutHours
import com.memandis.remote.utils.bookingutils.getNextBusinessDay
import com.memandis.remote.utils.bookingutils.parseDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentWidgetRemoteViewService: RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory? {

        return object : RemoteViewsFactory {
            private val scheduleList: MutableList<Scheduling?> = ArrayList()
            private var mDatabase: DatabaseReference? = null
            private var mAuth: FirebaseAuth? = null
            private var isRealTimeDataBaseInitialized = false
            override fun onCreate() {
                mDatabase = FirebaseDatabase.getInstance().reference
                mAuth = FirebaseAuth.getInstance()
            }

            private fun synService() {
                if (isRealTimeDataBaseInitialized) {
                    return
                }
                val currentUser = mAuth!!.currentUser
                if (currentUser != null && !currentUser.isAnonymous) {
                    isRealTimeDataBaseInitialized = true
                    val mSchedulingRef = mDatabase!!.child("professionalSchedule")
                    mSchedulingRef.orderByChild("idUserDay").equalTo(
                        geIdWithFormattedDateWithoutHours(
                            getNextBusinessDay(),
                            currentUser.uid
                        )
                    ).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            scheduleList.clear()
                            for (schedulingSnapshot in dataSnapshot.children) {
                                val scheduling: Scheduling? =
                                    schedulingSnapshot.getValue(Scheduling::class.java)
                                scheduleList.add(scheduling)
                            }
                            val dataUpdatedIntent =
                                Intent(AppointmentAppWidgetProvider.ACTION_DATA_UPDATED)
                            application.sendBroadcast(dataUpdatedIntent)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }

            override fun onDataSetChanged() {
                synService()
            }

            override fun onDestroy() {}
            override fun getCount(): Int {
                return scheduleList.size
            }

            override fun getViewAt(position: Int): RemoteViews? {
                if (position == AdapterView.INVALID_POSITION) {
                    return null
                }
                val views = RemoteViews(
                    packageName,
                    R.layout.widget_schedule_item
                )
                val scheduling: Scheduling? = scheduleList[position]
                val date: Date = parseDate(scheduling?.date!!)!!
                views.setTextViewText(R.id.txt_name, scheduling.professionalName)
                views.setTextViewText(R.id.txt_date, SimpleDateFormat("dd/MM/yyyy").format(date))
                views.setTextViewText(R.id.txt_hour, SimpleDateFormat("HH:mm").format(date))
                val fillInIntent = Intent()
                val professional = Professional(
                    scheduling.idProfessional,
                0L,
                    0L,
                    scheduling.professionalName,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
                )
                fillInIntent.putExtra(SchedulingActivity.EXTRA_PROFESSIONAL, professional)
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent)
                return views
            }

            override fun getLoadingView(): RemoteViews? {
                return null
            }

            override fun getViewTypeCount(): Int {
                return 1
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun hasStableIds(): Boolean {
                return true
            }
        }
    }

}
