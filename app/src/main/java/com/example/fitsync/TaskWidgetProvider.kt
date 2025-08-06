package com.example.fitsync

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class TaskWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widgets
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Get the task list from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("taskList", Context.MODE_PRIVATE)
            val tasksSet = sharedPreferences.getStringSet("tasks", emptySet())
            val taskList = tasksSet?.map { it.split("|")[0] } ?: listOf()

            // Create the widget layout
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Set task titles in the widget's TextViews
            if (taskList.isNotEmpty()) {
                views.setTextViewText(R.id.widgetTitle, "Your Tasks")

                // Loop through tasks and set each in a TextView (up to 5 tasks)
                taskList.take(5).forEachIndexed { index, taskName ->
                    val textViewId = context.resources.getIdentifier(
                        "widgetTaskItem$index",
                        "id",
                        context.packageName
                    )
                    views.setTextViewText(textViewId, taskName)
                }
            } else {
                views.setTextViewText(R.id.widgetTitle, "No tasks available")
            }

            // Define the pending intent to launch MainActivity when widget is clicked
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetTitle, pendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        // Call this function from MainActivity to update the widget whenever the task list changes
        fun refreshWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, TaskWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetTitle)
            appWidgetIds.forEach { appWidgetId ->
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
