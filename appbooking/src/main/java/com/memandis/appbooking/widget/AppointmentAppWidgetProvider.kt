package com.memandis.appbooking.widget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.memandis.appbooking.BookingActivity
import com.memandis.appbooking.R

import android.content.ComponentName
import androidx.annotation.NonNull
import com.memandis.appbooking.scheduling.SchedulingActivity

//AppWidgetProviderInfo object
//widget_next_schedule.xml

class AppointmentAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            // Perform this loop procedure for each App Widget that belongs to this provider
            appWidgetIds.forEach { appWidgetId ->
                // Create an Intent to launch ExampleActivity
                val pendingIntent: PendingIntent = Intent(context, BookingActivity::class.java)
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }

                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                val views: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.widget_schedule
                ).apply {
                    setOnClickPendingIntent(R.id.widget, pendingIntent)
                }

                // Set up the collection
                views.setRemoteAdapter(
                    R.id.widget_list,
                    Intent(context, AppointmentWidgetRemoteViewService::class.java)
                )

                val clickIntentTemplate =
                    if (true) Intent(context, SchedulingActivity::class.java)
                    else Intent( context, BookingActivity::class.java )
                val clickPendingIntentTemplate: PendingIntent = TaskStackBuilder
                    .create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate)
                views.setEmptyView(R.id.widget_list, R.id.widget_empty)

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

    override fun onReceive(@NonNull context: Context?, @NonNull intent: Intent) {
        super.onReceive(context, intent)
        if (ACTION_DATA_UPDATED == intent.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context!!, javaClass)
            )
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list)
        }
    }

    companion object {
        val ACTION_DATA_UPDATED = "com.memandis.appbooking.ACTION_DATA_UPDATED"
    }

}