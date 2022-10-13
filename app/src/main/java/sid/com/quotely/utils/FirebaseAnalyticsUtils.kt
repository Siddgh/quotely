package sid.com.quotely.utils

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import sid.com.quotely.constants.FirebaseConstants

object FirebaseAnalyticsUtils {
    private val firebaseAnalytics = Firebase.analytics

    fun logAdsDisplay(adType: String) {
        val params = Bundle()
        params.putString(FirebaseConstants.FirebaseAnalyticsLogAdsType, adType)
        firebaseAnalytics.logEvent(FirebaseConstants.FirebaseAnalyticsLogAdsType, params)
    }

    fun logPremiumPurchase() {
        val params = Bundle()
        params.putString(
            FirebaseConstants.FirebaseAnalyticsLogPremiumVersion,
            FirebaseConstants.FirebaseAnalyticsLogPremiumVersion
        )
        firebaseAnalytics.logEvent(
            FirebaseConstants.FirebaseAnalyticsLogPremiumVersion,
            params
        )
    }

    fun setAppVersionType(appType: String) {
        firebaseAnalytics.setUserProperty(
            FirebaseConstants.FirebaseAnalyticsUserProperty,
            appType
        )
    }

    fun logTypeClicks(movieName: String, type: String) {
        val params = Bundle()
        params.putString(FirebaseConstants.typeKey, movieName)
        params.putString(FirebaseConstants.FirebaseAnalyticsLogClickType, type)
        firebaseAnalytics.logEvent(
            FirebaseConstants.FirebaseAnalyticsLogTypeClick,
            params
        )
    }

    fun logTagsClicks(tagName: String, type: String) {
        val params = Bundle()
        params.putString(FirebaseConstants.tagKey, tagName)
        params.putString(FirebaseConstants.FirebaseAnalyticsLogClickType, type)
        firebaseAnalytics.logEvent(
            FirebaseConstants.FirebaseAnalyticsLogTagsClick,
            params
        )
    }

    fun logTypeClicks(type: String) {
        val params = Bundle()
        params.putString(FirebaseConstants.FirebaseAnalyticsLogClickType, type)
        firebaseAnalytics.logEvent(
            FirebaseConstants.FirebaseAnalyticsLogTagsClick,
            params
        )
    }

    fun logQuoteViewedClicks(movieName: String, type: String) {
        val params = Bundle()
        params.putString(FirebaseConstants.typeKey, movieName)
        params.putString(FirebaseConstants.FirebaseAnalyticsLogClickType, type)
        firebaseAnalytics.logEvent(
            FirebaseConstants.FirebaseAnalyticsLogQuoteViewClick,
            params
        )
    }

    fun setWidgetUsedProperty(isUsing: Boolean) {
        if (isUsing) {
            firebaseAnalytics.setUserProperty(
                FirebaseConstants.FirebaseAnalyticsUsingWidgetUserProperty,
                FirebaseConstants.FirebaseAnalyticsWidgetUsed
            )
        } else {
            firebaseAnalytics.setUserProperty(
                FirebaseConstants.FirebaseAnalyticsUsingWidgetUserProperty,
                FirebaseConstants.FirebaseAnalyticsWidgetNotUsed
            )
        }
    }

    fun logQuoteAction(action: String) {
        val params = Bundle()
        params.putString(FirebaseConstants.FirebaseAnalyticsLogClickType, action)
        firebaseAnalytics.logEvent(
            FirebaseConstants.FirebaseAnalyticsLogActionOnQuote,
            params
        )
    }
}