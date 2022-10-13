package sid.com.quotely.utils

import android.app.Activity
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import sid.com.quotely.adapters.*
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.contents.Favourites
import sid.com.quotely.models.data.FavouritesMeta
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.models.data.QuotesMeta
import sid.com.quotely.models.groupie.FavouritesTypeChipsGroupieModel
import sid.com.quotely.models.groupie.QuotesMoreGroupieModel
import java.io.IOException
import java.util.*

object CommonViewsUtils {

    fun setUpMoreOptions(
        from: String,
        tags: String,
        activity: Activity,
        type: String,
        recyclerView: RecyclerView
    ) {
        val items = mutableListOf<Item>()

        items.add(
            QuotesMoreGroupieModel(
                StringUtils.getMoreTitles(
                    GenericConstants.CONTENT,
                    mutableListOf(from)
                ), tags, from, activity, type
            )
        )

        items.add(
            QuotesMoreGroupieModel(
                StringUtils.getMoreTitles(
                    GenericConstants.TYPE,
                    mutableListOf(type)
                ), tags, from, activity, type
            )
        )

        items.add(
            QuotesMoreGroupieModel(
                StringUtils.getMoreTitles(
                    GenericConstants.TAG,
                    mutableListOf(tags)
                ), tags, from, activity, type
            )
        )

        val section = Section(items)
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
            }
            isNestedScrollingEnabled = false
        }
    }

    fun setUpFavouritesTypeChips(recyclerView: RecyclerView, activity: Favourites) {
        val items = mutableListOf<Item>()

        items.add(FavouritesTypeChipsGroupieModel(GenericConstants.ALL, activity))
        items.add(FavouritesTypeChipsGroupieModel(GenericConstants.MOVIE, activity))
        items.add(FavouritesTypeChipsGroupieModel(GenericConstants.TVSHOW, activity))
        items.add(FavouritesTypeChipsGroupieModel(GenericConstants.ANIME, activity))
        items.add(FavouritesTypeChipsGroupieModel(GenericConstants.BOOK, activity))
        items.add(FavouritesTypeChipsGroupieModel(GenericConstants.LEGEND, activity))

        val section = Section(items)
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
            }
            isNestedScrollingEnabled = false
        }
    }

    fun setUpRecentlyAddedSection(
        activity: Activity,
        textView: TextView,
        recyclerView: RecyclerView,
        type: String,
        options: FirebaseRecyclerOptions<MoviesMeta>
    ) {
        textView.text = StringUtils.getRecentlyAddedHeader(type)

        val recentMoviesListAdapter = RecentMoviesListAdapter(options, activity)

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(activity.baseContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentMoviesListAdapter
            isNestedScrollingEnabled = false
        }

    }

    fun setUpPopularQuotesSection(
        activity: Activity,
        textView: TextView,
        recyclerView: RecyclerView,
        type: String,
        options: FirestoreRecyclerOptions<QuotesMeta>,
        supportFragmentManager: FragmentManager
    ) {
        textView.text = StringUtils.getPopularQuotesHeader(type)

        val popularQuotesListAdapter =
            PopularQuotesListAdapter(options, activity.baseContext, supportFragmentManager)

        recyclerView.apply {
            isNestedScrollingEnabled = false
            adapter = popularQuotesListAdapter
            layoutManager =
                LinearLayoutManager(activity.baseContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun setUpTagsSection(
        activity: Activity,
        textView: TextView,
        recyclerView: RecyclerView,
        tag: String,
        options: FirestorePagingOptions<QuotesMeta>,
        supportFragmentManager: FragmentManager
    ) {
        textView.text = tag.toUpperCase(Locale.ROOT)

        val tagsListAdapter =
            TagsListAdapter(options, supportFragmentManager, activity.baseContext)

        recyclerView.apply {
            isNestedScrollingEnabled = false
            adapter = tagsListAdapter
            layoutManager =
                LinearLayoutManager(activity.baseContext, LinearLayoutManager.VERTICAL, false)
        }
    }


    fun setUpFavouriteQuotesSection(
        options: FirebaseRecyclerOptions<FavouritesMeta>,
        recyclerView: RecyclerView,
        activity: Activity,
        supportFragmentManager: FragmentManager
    ) {
        recyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager =
                LinearLayoutManager(activity.baseContext, LinearLayoutManager.VERTICAL, false)
            adapter = FavouritesListAdapter(options, activity, supportFragmentManager)
            hasFixedSize()
        }
    }

    fun setUpTypeSectionList(
        recyclerView: RecyclerView,
        options: DatabasePagingOptions<MoviesMeta>,
        activity: Activity
    ) {
        recyclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(activity.baseContext, 3)
            adapter = TypeSectionListAdapter(options, activity)
            hasFixedSize()
        }
    }

    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

}