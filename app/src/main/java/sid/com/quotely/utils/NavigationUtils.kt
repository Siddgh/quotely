package sid.com.quotely.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import sid.com.quotely.ProgressDialog
import sid.com.quotely.bottomsheet.QuotesBottomSheet
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.contents.*
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.models.data.QuotesMeta

object NavigationUtils {

    fun showQuotesBottomSheetFragment(
        supportFragmentManager: FragmentManager,
        quotesBottomSheet: QuotesBottomSheet,
        quotesMeta: QuotesMeta?
    ) {
        val dataToPass: ArrayList<String> = ArrayList()
        dataToPass.add(quotesMeta?.quote ?: FirebaseConstants.anon)
        dataToPass.add(quotesMeta?.from ?: FirebaseConstants.anon)
        dataToPass.add(quotesMeta?.tag ?: FirebaseConstants.anon)
        dataToPass.add(quotesMeta?.likes.toString())
        dataToPass.add(quotesMeta?.quoteId ?: FirebaseConstants.anon)
        dataToPass.add(quotesMeta?.type ?: FirebaseConstants.anon)
        val args = Bundle()
        args.putStringArrayList(GenericConstants.PassQuotesInformation, dataToPass)
        quotesBottomSheet.arguments = args
        quotesBottomSheet.show(supportFragmentManager, quotesBottomSheet.tag)
    }

    fun goToTypeSection(context: Context, type: String) {
        FirebaseAnalyticsUtils.logTypeClicks(type)
        val intent = Intent(context, TypeSection::class.java)
        intent.putExtra(GenericConstants.TYPE, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToTypeSectionList(context: Context, type: String) {
        val intent = Intent(context, TypeSectionList::class.java)
        intent.putExtra(GenericConstants.TYPE, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToTypeSectionListSearch(context: Context, type: String) {
        val intent = Intent(context, TypeSectionListSearch::class.java)
        intent.putExtra(GenericConstants.TYPE, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToIndividualTypeSection(context: Context, moviesMeta: MoviesMeta) {
        FirebaseAnalyticsUtils.logTypeClicks(moviesMeta.from ?: "", moviesMeta.type ?: "")
        val intent = Intent(context, IndividualTypeSection::class.java)
        intent.putExtra(GenericConstants.TYPE, moviesMeta.type)
        intent.putExtra(GenericConstants.FROM, moviesMeta.from)
        intent.putExtra(GenericConstants.FROMID, moviesMeta.fromId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToTagsList(context: Context, type: String, tag: String) {
        FirebaseAnalyticsUtils.logTagsClicks(tag, type)
        val intent = Intent(context, TagsList::class.java)
        intent.putExtra(GenericConstants.TYPE, type)
        intent.putExtra(GenericConstants.TAG, tag)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun AddToIndividualTypeSection(activity: Activity, model: MoviesMeta) {
        val dialog = ProgressDialog(activity)
        AdsUtils.displayInterstitialAd(activity.baseContext, dialog) {
            goToIndividualTypeSection(activity.baseContext, model)
        }
    }

    fun AddToTagsList(activity: Activity, type: String, tag: String) {
        val dialog = ProgressDialog(activity)
        AdsUtils.displayInterstitialAd(activity.baseContext, dialog) {
            goToTagsList(activity.baseContext, type, tag)
        }
    }

}