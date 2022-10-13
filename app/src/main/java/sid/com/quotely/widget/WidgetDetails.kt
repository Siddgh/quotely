package sid.com.quotely.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import sid.com.quotely.R
import sid.com.quotely.SignIn
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.contents.Home
import sid.com.quotely.utils.FirebaseAnalyticsUtils
import sid.com.quotely.utils.FirestoreUtils

class WidgetDetails : AppWidgetProvider() {

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Toast.makeText(context, "Widget updates every week", Toast.LENGTH_LONG).show()
        FirebaseAnalyticsUtils.setWidgetUsedProperty(true)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        FirebaseAnalyticsUtils.setWidgetUsedProperty(false)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        var intent: Intent?
        appWidgetIds?.forEach { appWidgetId ->
            intent = if (FirebaseAuth.getInstance().currentUser != null) {
                Intent(context, Home::class.java)
            } else {
                Intent(context, SignIn::class.java)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                GenericConstants.WidgetsToAppRequestCode,
                intent,
                0
            )

            val views = RemoteViews(
                context?.packageName,
                R.layout.widget_layout
            )
            views.setOnClickPendingIntent(R.id.ll_widget_layout, pendingIntent)
            if (FirebaseAuth.getInstance().currentUser != null) {
                FirestoreUtils.readQuoteOfTheWeek {
                    val quotesMeta = it
                    val quote = quotesMeta?.quote?.replace("...", "\n")
                    views.setCharSequence(R.id.tv_widget_layout_quote, "setText", quote)
                    views.setCharSequence(R.id.tv_widget_layout_movie, "setText", quotesMeta?.from)
                    appWidgetManager?.updateAppWidget(appWidgetId, views)
                }
            } else {
                views.setCharSequence(
                    R.id.tv_widget_layout_quote,
                    "setText",
                    "You're not logged in\n\nPlease sign in to the app for Quotes to be displayed here\n\nOnce Signed In, Create a new widget"
                )
                views.setCharSequence(R.id.tv_widget_layout_movie, "setText", "")
                views.setViewVisibility(R.id.iv_widget_layout_movie, View.GONE)
                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }
        }
    }
}