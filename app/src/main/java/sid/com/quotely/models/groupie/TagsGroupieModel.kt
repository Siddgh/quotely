package sid.com.quotely.models.groupie

import android.app.Activity
import android.content.Context
import android.util.Log
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.layout_tags_chip.view.*
import sid.com.quotely.R
import sid.com.quotely.contents.IndividualTypeSection
import java.util.*

class TagsGroupieModel(
    private val tag: String,
    private val movie: String,
    private val context: Context
) : Item() {
    override fun getLayout() = R.layout.layout_tags_chip

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val tag = tag
        val toDisplay = tag.toLowerCase(Locale.ENGLISH)
        viewHolder.itemView.apply {
            tv_layout_tags.text = toDisplay
            tv_layout_tags.setOnClickListener {
                val activity = context as IndividualTypeSection
                activity.setUpQuotesRecyclerView(movie, tag)
            }

        }
    }
}