package sid.com.quotely.models.groupie

import android.app.Activity
import android.content.res.ColorStateList
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.layout_tags_chip.view.*
import sid.com.quotely.R
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.utils.NavigationUtils
import sid.com.quotely.utils.StringUtils
import sid.com.quotely.utils.StringUtils.movieNameAsId

class QuotesMoreGroupieModel(
    private val toDisplay: String,
    private val tags: String,
    private val movie: String,
    private val activity: Activity,
    private val type: String
) : Item() {

    override fun getLayout() = R.layout.layout_tags_chip

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_layout_tags.text = toDisplay

        viewHolder.itemView.tv_layout_tags.setBackgroundColor(
            StringUtils.getTypeColor(
                activity.baseContext,
                type
            )
        )

        viewHolder.itemView.cv_layout_tags.setCardBackgroundColor(
            StringUtils.getTypeColor(
                activity.baseContext,
                type
            )
        )

        viewHolder.itemView.setOnClickListener {
            when (position) {
                0 -> {
                    NavigationUtils.AddToIndividualTypeSection(
                        activity,
                        MoviesMeta(fromId = movie.movieNameAsId(), from = movie, type = type)
                    )
                }
                1 -> {
                    NavigationUtils.goToTypeSection(activity.baseContext, type)
                }
                2 -> {
                    NavigationUtils.AddToTagsList(activity, type, tags)
                }
            }
        }
    }

}