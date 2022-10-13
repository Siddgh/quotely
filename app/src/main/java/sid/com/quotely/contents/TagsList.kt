package sid.com.quotely.contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.activity_tags_list.*
import kotlinx.android.synthetic.main.merge_recyclerview_with_header.view.*
import sid.com.quotely.R
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.FavouritesMeta
import sid.com.quotely.models.data.QuotesMeta
import sid.com.quotely.utils.AdsUtils
import sid.com.quotely.utils.CommonViewsUtils
import sid.com.quotely.utils.FirestoreUtils
import java.util.*

class TagsList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_list)
        val type = intent.getStringExtra(GenericConstants.TYPE) ?: ""
        val tag = intent.getStringExtra(GenericConstants.TAG) ?: ""

        displayAds()
        setUpPopularQuotes(tag, type)
    }

    private fun displayAds() {
        AdsUtils.displayBannerAd(context = this, adView = include_tags_list.banner_list)
    }

    private fun setUpPopularQuotes(tag: String, type: String) {


        val tagsQuotesOptions: FirestorePagingOptions<QuotesMeta> =
            FirestorePagingOptions.Builder<QuotesMeta>().setLifecycleOwner(this).setQuery(
                FirestoreUtils.fetchQuotesByTags(tag = tag, type = type),
                FirestoreUtils.quotesConfigFetch,
                QuotesMeta::class.java
            ).build()

        CommonViewsUtils.setUpTagsSection(
            this,
            include_tags_list.tv_recyclerview_with_header,
            include_tags_list.rv_recyclerview_with_header,
            tag,
            tagsQuotesOptions,
            supportFragmentManager
        )
    }

}
