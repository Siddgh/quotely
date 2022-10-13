package sid.com.quotely.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import kotlinx.android.synthetic.main.layout_quotes_card_grid.view.*
import sid.com.quotely.R
import sid.com.quotely.bottomsheet.QuotesBottomSheet
import sid.com.quotely.models.data.QuotesMeta
import sid.com.quotely.utils.NavigationUtils
import sid.com.quotely.utils.RealtimeDatabaseUtils
import sid.com.quotely.utils.StringUtils
import sid.com.quotely.utils.StringUtils.quote

class QuotesListAdapter(
    options: FirestorePagingOptions<QuotesMeta>,
    private val supportFragmentManager: FragmentManager
) :
    FirestorePagingAdapter<QuotesMeta, QuotesListAdapter.ViewHolder>(options) {

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_quotes_card_grid,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: QuotesMeta) {
        holder.itemView.apply {
            tv_quote_list_card_quote.text = model.quote.quote()
            tv_quote_list_likes.text = StringUtils.displayLikes(model.likes)
            tv_quote_list_card_category.text = model.tag
            iv_quote_list_movie.setImageResource(StringUtils.getTypeIcon(model.type))
            tv_quote_list_movie.text = model.from

            RealtimeDatabaseUtils.hasUserLikedQuote(model.quoteId) {
                if (it) {
                    iv_quotes_list_likes.setImageResource(R.drawable.ic_fav_filled)
                } else {
                    iv_quotes_list_likes.setImageResource(R.drawable.ic_fav)
                }
            }

            cv_quote_list_card.setOnClickListener {
                val bottomSheetFragment = QuotesBottomSheet()
                NavigationUtils.showQuotesBottomSheetFragment(
                    supportFragmentManager,
                    bottomSheetFragment,
                    model
                )
            }
        }
    }


}