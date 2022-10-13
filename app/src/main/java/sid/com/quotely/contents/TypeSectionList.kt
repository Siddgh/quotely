package sid.com.quotely.contents

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.paging.DatabasePagingOptions
import kotlinx.android.synthetic.main.activity_type_section_list.*
import sid.com.quotely.R
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.utils.AdsUtils
import sid.com.quotely.utils.CommonViewsUtils
import sid.com.quotely.utils.NavigationUtils
import sid.com.quotely.utils.RealtimeDatabaseUtils

class TypeSectionList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_section_list)

        displayAds()

        val type = intent.getStringExtra(GenericConstants.TYPE) ?: ""
        setUpViews(type)

        tv_type_section_search.setOnClickListener {
            NavigationUtils.goToTypeSectionListSearch(this, type)
        }

    }


    private fun displayAds() {
        AdsUtils.displayBannerAd(context = this, adView = banner_type_section_list)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews(type: String) {
        tv_type_section_header.text = "${type}s"

        val options =
            DatabasePagingOptions.Builder<MoviesMeta>().setLifecycleOwner(this).setQuery(
                RealtimeDatabaseUtils.fetchTypeSectionListDatabaseReference(type),
                RealtimeDatabaseUtils.pagingConfig,
                MoviesMeta::class.java
            ).build()

        CommonViewsUtils.setUpTypeSectionList(rv_type_section_list, options, this)
    }
}
