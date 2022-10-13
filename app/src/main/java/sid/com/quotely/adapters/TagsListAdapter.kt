package sid.com.quotely.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import kotlinx.android.synthetic.main.layout_quotes_card_list.view.*
import sid.com.quotely.R
import sid.com.quotely.bottomsheet.QuotesBottomSheet
import sid.com.quotely.models.data.QuotesMeta
import sid.com.quotely.utils.NavigationUtils
import sid.com.quotely.utils.StringUtils

class TagsListAdapter(
    options: FirestorePagingOptions<QuotesMeta>,
    private val supportFragmentManager: FragmentManager,
    private val c: Context
) :
    FirestorePagingAdapter<QuotesMeta, TagsListAdapter.ViewHolder>(options) {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: QuotesMeta) {
        holder.itemView.apply {
            tv_layout_quotes_card_list_quote.text = model.quote
            tv_layout_quotes_card_list_from.text = model.from
            tv_layout_quotes_card_list_likes.text = StringUtils.displayLikes(model.likes)
            tv_layout_quotes_card_list_tag.text = model.tag

            iv_layout_quotes_card_list_from.setImageResource(StringUtils.getTypeIcon(model.type))

            view_layout_quotes_card_list_indicator.setBackgroundColor(
                StringUtils.getTypeColor(
                    c,
                    model.type
                )
            )

            cv_layout_quotes_card_list_tag.setCardBackgroundColor(
                ColorStateList.valueOf(
                    StringUtils.getTypeColor(
                        c,
                        model.type
                    )
                )
            )

            setOnClickListener {
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