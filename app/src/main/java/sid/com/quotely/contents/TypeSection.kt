package sid.com.quotely.contents

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.activity_type_section.*
import kotlinx.android.synthetic.main.merge_recyclerview_with_header.view.*
import sid.com.quotely.R
import sid.com.quotely.adapters.TagsPagingAdapter
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.models.data.QuotesMeta
import sid.com.quotely.models.data.TagsListMeta
import sid.com.quotely.utils.*

class TypeSection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_section)
        displayAds()
        val type = intent.getStringExtra(GenericConstants.TYPE) ?: ""
        setUpViews(type)
        setUpRecentlyAddedMovies(type)
        setUpPopularQuotes(type)
        setUpTagsPagingRecyclerView(type)

        cv_movies_all_text_quotes.setOnClickListener {
            NavigationUtils.goToTypeSectionList(this, type)
        }
    }


    private fun displayAds() {
        AdsUtils.displayBannerAd(context = this, adView = banner_type_section)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpTagsPagingRecyclerView(type: String) {

        include_type_section_explore_list.tv_recyclerview_with_header.text =
            "$type Quotes by Category"

        val options = DatabasePagingOptions.Builder<TagsListMeta>().setLifecycleOwner(this)
            .setQuery(
                RealtimeDatabaseUtils.fetchTagsForTypeDatabaseReference(type),
                RealtimeDatabaseUtils.pagingConfig,
                TagsListMeta::class.java
            ).build()

        val tagsPagingAdapter = TagsPagingAdapter(options, this, type)
        include_type_section_explore_list.rv_recyclerview_with_header.apply {
            layoutManager =
                LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            adapter = tagsPagingAdapter
            isNestedScrollingEnabled = false
        }

    }

    private fun setUpPopularQuotes(type: String) {

        val popularQuotesOptions =
            FirestoreRecyclerOptions.Builder<QuotesMeta>().setLifecycleOwner(this)
                .setQuery(
                    FirestoreUtils.fetchPopularQuotes(type),
                    QuotesMeta::class.java
                )
                .build()

        CommonViewsUtils.setUpPopularQuotesSection(
            this,
            include_type_section_popular_quotes.tv_recyclerview_with_header,
            include_type_section_popular_quotes.rv_recyclerview_with_header,
            type,
            popularQuotesOptions,
            supportFragmentManager
        )
    }

    private fun setUpRecentlyAddedMovies(type: String) {
        val options =
            FirebaseRecyclerOptions.Builder<MoviesMeta>().setLifecycleOwner(this)
                .setQuery(
                    RealtimeDatabaseUtils.fetchRecentlyAddedQuotes(type),
                    MoviesMeta::class.java
                )
                .build()

        CommonViewsUtils.setUpRecentlyAddedSection(
            this,
            include_type_section_recently_added.tv_recyclerview_with_header,
            include_type_section_recently_added.rv_recyclerview_with_header,
            type,
            options
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews(type: String) {
        tv_activity_type_section_header.text = "${type}s"

        cv_movies_all_text_quotes.setCardBackgroundColor(
            ColorStateList.valueOf(
                StringUtils.getTypeColor(
                    this,
                    type
                )
            )
        )


    }
}
