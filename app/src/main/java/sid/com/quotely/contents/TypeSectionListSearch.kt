package sid.com.quotely.contents

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_type_section_list_search.*
import sid.com.quotely.R
import sid.com.quotely.adapters.TypeSectionSearchListAdapter
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.utils.AdsUtils
import sid.com.quotely.utils.RealtimeDatabaseUtils
import sid.com.quotely.utils.RealtimeDatabaseUtils.searchListDatabaseReference
import sid.com.quotely.utils.StringUtils
import sid.com.quotely.utils.StringUtils.movieNameAsId

class TypeSectionListSearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_section_list_search)

        displayAds()

        val type = intent.getStringExtra(GenericConstants.TYPE) ?: ""
        setUpViews(type)
        ed_type_section_search.requestFocus()
        val imgr: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(ed_type_section_search, InputMethodManager.SHOW_IMPLICIT)
        tv_type_section_search_listing.text = StringUtils.searchMessage(type)


        ed_type_section_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    setupRecyclerView(s.toString(), type)
                    tv_type_section_search_listing.text =
                        StringUtils.searchingForMessage(s.toString())
                } else {
                    tv_type_section_search_listing.text = StringUtils.searchMessage(type)
                }
            }

        })
    }

    private fun setupRecyclerView(searchText: String, type: String) {

        val options: FirebaseRecyclerOptions<MoviesMeta> =
            FirebaseRecyclerOptions.Builder<MoviesMeta>().setLifecycleOwner(this).setQuery(
                RealtimeDatabaseUtils.fetchMoviesBySearchFirebaseDatabase(searchText, type),
                MoviesMeta::class.java
            ).build()

        rv_type_section_search_list.apply {
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(baseContext, 3)
            adapter = TypeSectionSearchListAdapter(options, this@TypeSectionListSearch)
            hasFixedSize()
        }
    }


    private fun displayAds() {
        AdsUtils.displayBannerAd(context = this, adView = banner_type_section_list_search)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews(type: String) {
        tv_type_section_search_big_title.text = "${type}s"
    }
}
