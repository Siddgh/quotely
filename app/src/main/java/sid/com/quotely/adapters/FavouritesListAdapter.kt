package sid.com.quotely.adapters

import android.app.Activity
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import com.firebase.ui.database.paging.LoadingState
import kotlinx.android.synthetic.main.layout_quotes_card_list.view.*
import sid.com.quotely.R
import sid.com.quotely.bottomsheet.QuotesBottomSheet
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.FavouritesMeta
import sid.com.quotely.utils.FirestoreUtils
import sid.com.quotely.utils.NavigationUtils
import sid.com.quotely.utils.StringUtils

class FavouritesListAdapter(
    options: FirebaseRecyclerOptions<FavouritesMeta>,
    private val activity: Activity,
    private val supportFragmentManager: FragmentManager
) :
    FirebaseRecyclerAdapter<FavouritesMeta, FavouritesListAdapter.ViewHolder>(options) {

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_quotes_card_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: FavouritesMeta) {
        FirestoreUtils.getQuoteFromFirestore(model.quoteId) {
            holder.itemView.apply {
                val quotesMeta = it
                tv_layout_quotes_card_list_quote.text = quotesMeta?.quote
                tv_layout_quotes_card_list_from.text = quotesMeta?.from
                tv_layout_quotes_card_list_likes.text =
                    StringUtils.displayLikes(quotesMeta?.likes ?: 0)
                tv_layout_quotes_card_list_tag.text = quotesMeta?.tag

                iv_layout_quotes_card_list_from.setImageResource(
                    StringUtils.getTypeIcon(
                        quotesMeta?.type ?: GenericConstants.MOVIE
                    )
                )

                view_layout_quotes_card_list_indicator.setBackgroundColor(
                    StringUtils.getTypeColor(
                        activity.baseContext,
                        quotesMeta?.type ?: GenericConstants.MOVIE
                    )
                )

                cv_layout_quotes_card_list_tag.setCardBackgroundColor(
                    ColorStateList.valueOf(
                        StringUtils.getTypeColor(
                            activity.baseContext,
                            quotesMeta?.type ?: GenericConstants.MOVIE
                        )
                    )
                )

                setOnClickListener {
                    val bottomSheetFragment = QuotesBottomSheet()
                    NavigationUtils.showQuotesBottomSheetFragment(
                        supportFragmentManager,
                        bottomSheetFragment,
                        quotesMeta
                    )
                }
            }
        }
    }
}