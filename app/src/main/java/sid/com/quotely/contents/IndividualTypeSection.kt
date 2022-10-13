package sid.com.quotely.contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_individual_type_section.*
import sid.com.quotely.R
import sid.com.quotely.adapters.QuotesListAdapter
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.models.data.QuotesMeta
import sid.com.quotely.models.data.TagsListMeta
import sid.com.quotely.models.groupie.TagsGroupieModel
import sid.com.quotely.utils.*

class IndividualTypeSection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_type_section)
        displayAds()
        val type = intent.getStringExtra(GenericConstants.TYPE) ?: ""
        val from = intent.getStringExtra(GenericConstants.FROM) ?: ""
        val fromId = intent.getStringExtra(GenericConstants.FROMID) ?: ""
        val moviesMeta = MoviesMeta(type = type, from = from, fromId = fromId)
        setUpViews(moviesMeta)
        setUpCategoriesRecyclerView(moviesMeta)
    }

    private fun displayAds() {
        AdsUtils.displayBannerAd(context = this, adView = banner_individual_type_section)
    }

    private fun setUpCategoriesRecyclerView(moviesMeta: MoviesMeta) {

        setUpQuotesRecyclerView(moviesMeta.from ?: FirebaseConstants.anon, GenericConstants.ALL)

        val items = mutableListOf<Item>()
        items.add(
            TagsGroupieModel(
                GenericConstants.ALL,
                moviesMeta.from ?: FirebaseConstants.anon,
                baseContext
            )
        )

        RealtimeDatabaseUtils.getTagsFromMovies(
            moviesMeta.fromId ?: FirebaseConstants.anon,
            moviesMeta.type ?: FirebaseConstants.anon
        ).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (tag in p0.children) {

                        items.add(
                            TagsGroupieModel(
                                tag.value.toString(),
                                moviesMeta.from ?: FirebaseConstants.anon,
                                baseContext
                            )
                        )

                        val section = Section(items)

                        rv_activity_individual_type_section_category_tags.apply {
                            layoutManager =
                                LinearLayoutManager(
                                    baseContext,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            adapter = GroupAdapter<GroupieViewHolder>().apply {
                                add(section)
                            }
                            isNestedScrollingEnabled = false
                        }
                    }
                }
            }

        })

    }

    fun setUpQuotesRecyclerView(from: String, tag: String) {
        Log.d("DEBUGGING", "Tag: $tag")
        tv_activity_individual_type_section_list.text =
            StringUtils.getIndividualTypeSectionTitle(tag)

        val quotesOptions: FirestorePagingOptions<QuotesMeta> =
            FirestorePagingOptions.Builder<QuotesMeta>().setLifecycleOwner(this).setQuery(
                FirestoreUtils.getQuotesFromMovieAndTagFirestore(from, tag),
                FirestoreUtils.quotesConfigFetch,
                QuotesMeta::class.java
            ).build()

        val quotesListAdapter = QuotesListAdapter(
            options = quotesOptions,
            supportFragmentManager = supportFragmentManager
        )

        rv_activity_individual_type_section_quotes_list.apply {
            isNestedScrollingEnabled = false
            adapter = quotesListAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    private fun setUpViews(moviesMeta: MoviesMeta) {

        Glide.with(baseContext)
            .load(
                FirebaseStorageUtils.getMoviePosterReferenceById(
                    moviesMeta.fromId ?: FirebaseConstants.anon, moviesMeta.type ?: ""
                )
            )
            .centerCrop()
            .into(iv_individual_type_section_card)


        tv_individual_type_section_title.text = moviesMeta.from
    }
}
