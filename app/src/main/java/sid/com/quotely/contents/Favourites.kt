package sid.com.quotely.contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.DatabasePagingOptions
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.merge_recyclerview_with_header.view.*
import sid.com.quotely.R
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.FavouritesMeta
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.utils.AdsUtils
import sid.com.quotely.utils.CommonViewsUtils
import sid.com.quotely.utils.RealtimeDatabaseUtils
import sid.com.quotely.utils.StringUtils

class Favourites : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        displayAds()

        tv_activity_favourites_title.text =
            StringUtils.getFavouriteQuotesTitle(GenericConstants.ALL)

        CommonViewsUtils.setUpFavouritesTypeChips(rv_activity_favourites_type, this)

        setUpFavouritesRecyclerList(GenericConstants.ALL)

    }

    private fun displayAds() {
        AdsUtils.displayBannerAd(context = this, adView = banner_activity_favourites)
    }

    fun setUpFavouritesRecyclerList(type: String) {
        val options =
            FirebaseRecyclerOptions.Builder<FavouritesMeta>().setLifecycleOwner(this)
                .setQuery(
                    RealtimeDatabaseUtils.fetchUserLikedQuotes(type),
                    FavouritesMeta::class.java
                )
                .build()

        CommonViewsUtils.setUpFavouriteQuotesSection(
            options,
            rv_activity_favourties_list,
            this,
            supportFragmentManager
        )
    }
}
